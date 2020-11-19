package com.stylefeng.guns.rest.promo.rocketmq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoStockMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromoStock;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class MqConsumer {

    @Value("${mq.nameserver.address}")
    private String address;

    @Value("${mq.topic}")
    private String topic;

    @Autowired
    MtimePromoStockMapper mtimePromoStockMapper;

    private DefaultMQPushConsumer mqPushConsumer;
    @PostConstruct
    public void init1() throws MQClientException {
        //初始化消费者以及group
        mqPushConsumer = new DefaultMQPushConsumer("consumer_group");
        //查找注册中心地址
        mqPushConsumer.setNamesrvAddr(address);
        //根据topic订阅消息
        mqPushConsumer.subscribe(topic,"*");
        //注册消息监听器
        mqPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                //获取消息
                MessageExt messageExt = list.get(0);
                //获取消息的消息体
                byte[] body = messageExt.getBody();
                String bodyStr = new String(body);
                //解析成原本的数据类型
                HashMap map = JSON.parseObject(bodyStr, HashMap.class);
                //取出map里的关键参数，用于更新数据库
                Integer promoId = (Integer) map.get("promoId");
                Integer amount = (Integer) map.get("amount");
                //更新数据库中的库存
                EntityWrapper<MtimePromoStock> stockEntityWrapper = new EntityWrapper<>();
                stockEntityWrapper.eq("promo_id",promoId);
                int i = mtimePromoStockMapper.updateStockByPromoId(promoId, amount);
                if(i == 1){
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                //其余返回失败,有重试机制，默认16次
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });
        //开启消费者
        mqPushConsumer.start();
    }
}
