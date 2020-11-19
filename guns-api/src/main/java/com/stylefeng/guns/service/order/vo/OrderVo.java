package com.stylefeng.guns.service.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 构造响应报文beanVO
 */
@Data
public class OrderVo implements Serializable {
    private String orderId;//订单id
    private String filmName;//电影名
//    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private String fieldTime;//电影开场时间
    private String cinemaName;//影院名
    private String seatsName;//座位类型
    private String orderPrice;//订单总价
//    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private String orderTimestamp;//下单时间
    private String orderStatus;//订单状态 0-待支付 1-已支付 2-已关闭（取消）
}
