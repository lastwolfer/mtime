package com.stylefeng.guns.core.constant;

/**
 * @author: jia.xue
 * @create: 2019-06-17 15:20
 * @Description
 * 0-待支付,1-已支付,2-已关闭
 **/
public class OrderStatus extends BaseType {

    public OrderStatus(Integer index, String description) {
        super(index, description);
    }

    public static OrderStatus NOT_PAY = new OrderStatus(0,"未支付");
    public static OrderStatus PAYED = new OrderStatus(1,"已支付");
    public static OrderStatus CLOSED = new OrderStatus(2,"已关闭");


}