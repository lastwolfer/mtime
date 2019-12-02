package com.stylefeng.guns.rest.order.schedule;

import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Lazy(false)
public class CancelOrder{

    @Autowired
    MoocOrderTMapper moocOrderTMapper;

    /* 添加定时任务15分钟操作一次数据库
    1.fixedRate：定义一个按一定频率执行的定时任务，
    2.fixedDelay：定义一个按一定频率执行的定时任务，与上面不同的是，改属性可以配合initialDelay， 定义该任务延迟执行时间。
    3.cron：通过cronExpression来配置任务执行时间*/

    @Scheduled(cron = "0 0/1 * * * ?")
    private void cancel() {
        //定时执行的业务逻辑
        //查询未支付订单
        Map<String,Object> map = new HashMap<>();
        map.put("order_status", 0);
        List<MoocOrderT> orderList = moocOrderTMapper.selectByMap(map);
        //如果下单时间，距离现在超过10分钟，就将订单状态改为3.已过期
        for (MoocOrderT order : orderList) {
            if(System.currentTimeMillis() - order.getOrderTime().getTime() > 600000){
                order.setOrderStatus(2);
                moocOrderTMapper.cancelOrder(order.getUuid());
            }
        }
    }
}
