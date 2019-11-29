package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class HallInfoVo implements Serializable {

    private static final long serialVersionUID = 4522179378123280859L;

    private String hallFieldId;

    private String hallName;

    private Integer price;

    private String seatFile;

    private String soldSeats = "1,2,3,5,12";
}
