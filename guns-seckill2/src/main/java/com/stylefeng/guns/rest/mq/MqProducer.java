package com.stylefeng.guns.rest.mq;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeStockLogMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeStockLog;
import com.stylefeng.guns.rest.seckill.constant.StockLogStatus;
import com.stylefeng.guns.service.seckill.SeckillService;
import com.stylefeng.guns.service.seckill.vo.PromoOrderVO;
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

/**
 * @author: jia.xue
 * @create: 2019-10-21 10:01
 * @Description
 **/

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
    private String transactiongroup;


    private DefaultMQProducer mqProducer;


    private TransactionMQProducer transactionMQProducer;

    @Autowired
    private MtimeStockLogMapper stockLogMapper;

    @Autowired
    private SeckillService seckillService;


    @PostConstruct
    public void initProducer() {
        mqProducer = new DefaultMQProducer(groupName);
        mqProducer.setNamesrvAddr(address);
        try {
            mqProducer.start();
        } catch (MQClientException e) {
            e.printStackTrace();

        }
        log.info("mqProducer启动成功...");

        transactionMQProducer = new TransactionMQProducer(transactiongroup);
        transactionMQProducer.setNamesrvAddr(address);
        try {
            transactionMQProducer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }


        //设置一个事务监听回调器
        transactionMQProducer.setTransactionListener(new TransactionListener() {

            //第一个方法 执行本地事务
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg,Object args) {

                HashMap hashMap = (HashMap)args;
                Integer promoId = (Integer) hashMap.get("promoId");
                Integer amount = (Integer) hashMap.get("amount");
                Integer userId = (Integer) hashMap.get("userId");
                String stockLogId = (String) hashMap.get("stockLogId");

                PromoOrderVO promoOrderVO = null;
                try {
                    //执行本地事务  插入订单 扣减redis中的库存
                    promoOrderVO = seckillService.savePromoOrderVO(promoId, userId, amount,stockLogId);

                    //执行完成之后库存流水状态被更改

                } catch (Exception e) {
                    e.printStackTrace();
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                if (promoOrderVO == null) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }

                return LocalTransactionState.COMMIT_MESSAGE;
            }


            //回查本地事务状态
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {

                byte[] body = msg.getBody();
                String bodyStr = new String(body);
                HashMap hashMap = JSON.parseObject(bodyStr, HashMap.class);

                String stockLogId = (String) hashMap.get("stockLogId");
                MtimeStockLog stockLog = stockLogMapper.selectById(stockLogId);

                Integer status = stockLog.getStatus();
               //如果status 是成功 表示本地事务执行成功
                if (status == StockLogStatus.SUCCESS.getIndex()){
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                //如果status是失败，表示本地事务执行失败
                if (status == StockLogStatus.FAIL.getIndex()) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }

                return LocalTransactionState.UNKNOW;
            }
        });


    }


    public boolean asyncDecreaseStock(Integer promoId,Integer amount){

        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("promoId",promoId);
        hashMap.put("amount",amount);

        String jsonString = JSON.toJSONString(hashMap);
        Message message = new Message(topic, jsonString.getBytes(Charset.forName("utf-8")));
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(message);
        } catch (MQClientException e) {
            e.printStackTrace();
            return false;
        } catch (RemotingException e) {
            e.printStackTrace();
            return false;
        } catch (MQBrokerException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        if (sendResult == null) {
            return false;
        }
        SendStatus sendStatus = sendResult.getSendStatus();
        if (SendStatus.SEND_OK == sendStatus) {
            return true;
        }

        return false;
    }

    //发送事务型消息
    public Boolean sendStockMessageIntransaction(Integer promoId, Integer amount, Integer userId,String stockLogId){

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("promoId",promoId);
        hashMap.put("amount",amount);
        hashMap.put("userId",userId);
        hashMap.put("stockLogId",stockLogId);


        HashMap<String, Object> argsMap = new HashMap<>();
        argsMap.put("promoId",promoId);
        argsMap.put("amount",amount);
        argsMap.put("userId",userId);
        argsMap.put("stockLogId",stockLogId);

        String jsonString = JSON.toJSONString(hashMap);

        Message message = new Message(topic, jsonString.getBytes(Charset.forName("utf-8")));

        TransactionSendResult transactionSendResult = null;
        try {
            //发送事务型消息
            transactionSendResult = transactionMQProducer.sendMessageInTransaction(message, argsMap);
        } catch (MQClientException e) {
            e.printStackTrace();
            log.error("发送事务型消息—stock异常");
            return false;
        }

        if (transactionSendResult == null) {
            return false;
        }
        //本地事务执行状态
        LocalTransactionState localTransactionState = transactionSendResult.getLocalTransactionState();
        if (LocalTransactionState.COMMIT_MESSAGE.equals(localTransactionState)) {
            return true;
        }else{
            return false;
        }
    }

}
