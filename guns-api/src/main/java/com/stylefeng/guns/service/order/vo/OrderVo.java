package com.stylefeng.guns.service.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderVo implements Serializable {
    private String orderId;
    private String filmName;
//    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private String fieldTime;
    private String cinemaName;
    private String seatsName;
    private String orderPrice;
//    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private String orderTimestamp;
    private String orderStatus;
}
