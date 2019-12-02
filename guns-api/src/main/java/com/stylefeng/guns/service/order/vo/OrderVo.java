package com.stylefeng.guns.service.order.vo;


import lombok.Data;

@Data
public class OrderVo {

    private Integer orderId;
    private String filmName;
    private String fieldTime;
    private String cinemaName;
    private String seatsName;
    private Double orderPrice;
    private String orderTimestamp;

}
