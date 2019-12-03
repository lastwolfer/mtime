package com.stylefeng.guns.service.seckill.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SeckillVo implements Serializable {
    private static final long serialVersionUID = 2848347531987974754L;
    private int cinemaId;
    private double price;
    private String cinemaName;
    private String description;
    private String imgAddress;
    private String startTime;
    private String endTime;
    private int stock;
    private int uuid;
    private String cinemaAddress;
    private int status;
}
