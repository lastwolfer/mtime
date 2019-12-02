package com.stylefeng.guns.service.order;

import com.alibaba.dubbo.container.page.Page;
import com.stylefeng.guns.service.order.vo.OrderVo;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public interface MoocOrderTService {



    //验证售出的票是否为真
    boolean isTrueSeats(String fieldId,String seatId) throws FileNotFoundException, UnsupportedEncodingException;

    //已经销售的座位里，有没有这些座位
    boolean isNotSoldSeats(String fieldId,String seats);

    //创建订单信息
    OrderVo saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId);

//    //使用当前登陆用户获取已经购买的订单
//    Page<OrderVo> getOrderByUserId(Integer userId,Page<OrderVo> page);

    //根据FieldId 获取所有已经销售的座位编号
    String getSoldSeatsByFieldId(Integer fieldId);
}
