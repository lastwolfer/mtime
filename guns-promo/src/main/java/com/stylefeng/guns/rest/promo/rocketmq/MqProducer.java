package com.stylefeng.guns.rest.promo.rocketmq;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.core.constant.StockLogStatus;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeStockLogMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeStockLog;
import com.stylefeng.guns.service.promo.PromoService;
import com.stylefeng.guns.service.promo.vo.PromoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.HashMap;

@Component
@Slf4j
public class MqProducer {

    @Value("${mq.nameserver.address}")
    private String address;
    @Value("${mq.topic}")
    private String topic;
    @Value("${mq.producergroup}")
    private String groupName;
    @Value("${mq.transactionproducergroup}")
    private String transactiongroupName;

    private DefaultMQProducer producer;
    private TransactionMQProducer transactionMQProducer;//事务型消息生产者

    @Autowired
    private PromoService promoService;
    @Autowired
    private MtimeStockLogMapper mtimeStockLogMapper;

    /**
     * 在类加载时初始化生产者
     */
    @PostConstruct
    public void init(){
        //给默认型消息生产者初始化
        //设置生产者以及group
        producer = new DefaultMQProducer(groupName);
        //设置注册中心地址
        producer.setNamesrvAddr(address);
        //启动
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

        //给事务型消息生产者初始化
        transactionMQProducer = new TransactionMQProducer(transactiongroupName);
        transactionMQProducer.setNamesrvAddr(address);
        try {
            transactionMQProducer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        //设置一个事务监听回调器
        transactionMQProducer.setTransactionListener(new TransactionListener() {
            //第一个方法：执行本地事务
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                //取出MQserver中的参数值
                HashMap map = (HashMap) o;
                Integer promoId = (Integer) map.get("promoId");
                Integer amount = (Integer) map.get("amount");
                Integer userId = (Integer) map.get("userId");
                String stockLogId = (String) map.get("stockLogId");
                //本地事务：插入订单 扣减redis缓存库存
                PromoVo order = null;
                try {
                    order = promoService.createOrder(promoId, amount, userId,stockLogId);//执行完成之后库存流水状态被更改
                } catch (Exception e) {
                    e.printStackTrace();
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                if(order == null){
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                return LocalTransactionState.COMMIT_MESSAGE;
            }
            //第二个方法：回查本地事务状态方法
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                byte[] body = messageExt.getBody();
                String s = new String(body);
                HashMap map = JSON.parseObject(s, HashMap.class);

                String stockLogId = (String) map.get("stockLogId");
                MtimeStockLog mtimeStockLog = mtimeStockLogMapper.selectById(stockLogId);
                Integer status = mtimeStockLog.getStatus();
                //如果status是成功，则表示本地事务执行成功
                if(status == StockLogStatus.SUCCESS.getIndex()){
                    return  LocalTransactionState.COMMIT_MESSAGE;
                }
                //如果status是失败，则表示本地事务执行失败
                if(status == StockLogStatus.FAIL.getIndex()){
                    return  LocalTransactionState.ROLLBACK_MESSAGE;
                }
                //如果是初始值，表示还没执行本地事务
                return LocalTransactionState.UNKNOW;
            }
        });
        //检查有没有初始化
        log.info("produce初始化成功！address:{}",address);
    }

    /**向注册中心发送事务型消息
     * @param promoId
     * @param amount
     * @return true为发送成功，false为发送失败
     */
    public Boolean sendStockMessageIntransaction(Integer promoId,Integer amount,Integer userId,String stockLogId){
        HashMap<String, Object> map = new HashMap<>();
        //把这三个关键参数保存在hashmap中
        map.put("promoId",promoId);
        map.put("amount",amount);
        map.put("userId",userId);

        HashMap<String, Object> argMap = new HashMap<>();
        //把这三个关键参数保存在hashmap中
        argMap.put("promoId",promoId);
        argMap.put("amount",amount);
        argMap.put("userId",userId);
        argMap.put("stockLogId",stockLogId);
        //把map先转换成Json数据格式，再转换成byte数组构建成消息体;还要传入topic类型
        Message message = new Message(topic, JSON.toJSONString(map).getBytes(Charset.forName("utf-8")));
        //发送一个事务型消息，会返回一个结果
        TransactionSendResult  sendResult = null;
        try {
            sendResult = transactionMQProducer.sendMessageInTransaction(message, argMap);
        } catch (MQClientException e) {
            e.printStackTrace();
            log.error("发送事务型消息stock:失败");
            return false;
        }
        //测试一下
        log.info("发送事务型消息完成，发送结果为：{}", JSON.toJSONString(sendResult));
        //判断结果，看有没有发送成功
        if(sendResult == null){
            return false;
        } else {
            //发送成功！
            //查看本地事务的状态码，执行相应commit或者是rollback
            LocalTransactionState localTransactionState = sendResult.getLocalTransactionState();
            if(LocalTransactionState.COMMIT_MESSAGE.equals(localTransactionState)){
                return true;//成功
            }else {
                return false;
            }
        }
    }

    /**向注册中心发送消息(不是事务型消息)
     * @param promoId
     * @param amount
     * @return true为发送成功，false为发送失败
     */
    public Boolean sendDefaultMessage(Integer promoId,Integer amount){
        HashMap<String, Object> map = new HashMap<>();
        //把这两个关键参数保存在hashmap中
        map.put("promoId",promoId);
        map.put("amount",amount);
        //把map先转换成Json数据格式，再转换成byte数组构建成消息体;还要传入topic类型
        Message message = new Message(topic, JSON.toJSONString(map).getBytes());
        //发送消息，会返回一个结果
        SendResult sendResult = null;
        try {
            sendResult = producer.send(message);
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //测试一下
        log.info("发送消息完成，发送结果为：{}", JSON.toJSONString(sendResult));
        //判断结果，看有没有发送成功
        if(sendResult == null){
            return false;
        } else {
            //查看返回状态码
            SendStatus sendStatus = sendResult.getSendStatus();
            if(SendStatus.SEND_OK.equals(sendStatus)){
                return true;//成功
            }
            return false;
        }
    }
}
