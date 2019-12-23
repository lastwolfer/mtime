package com.stylefeng.guns.rest.mq;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoStockMapper;
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

/**
 * @author: jia.xue
 * @create: 2019-10-21 10:20
 * @Description
 **/
@Component
@Slf4j
public class MqConsumer {

    @Value("${mq.nameserver.address}")
    private String address;
    @Value("${mq.topic}")
    private String topic;
    @Value("${mq.consumergroup}")
    private String groupName;

    private DefaultMQPushConsumer mqPushConsumer;

    @Autowired
    private MtimePromoStockMapper stockMapper;

    @PostConstruct
    public void init(){
        mqPushConsumer = new DefaultMQPushConsumer(groupName);
        mqPushConsumer.setNamesrvAddr(address);

        try {
            mqPushConsumer.subscribe(topic,"*");
        } catch (MQClientException e) {
            e.printStackTrace();
            log.info("mqPushConsumer订阅失败！");
        }

        mqPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                MessageExt messageExt = msgs.get(0);
                byte[] body = messageExt.getBody();
                String jsonString = new String(body);

                HashMap<String,Integer> hashMap = JSON.parseObject(jsonString, HashMap.class);
                Integer promoId = hashMap.get("promoId");
                Integer amount = hashMap.get("amount");

                Integer affectedRows = stockMapper.decreaseStock(promoId, amount);
                if (affectedRows < 1){
                    log.info("消费失败！扣减库存失败，promoId:{},amount:{}",promoId,amount);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        try {
            mqPushConsumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }


    }
}
