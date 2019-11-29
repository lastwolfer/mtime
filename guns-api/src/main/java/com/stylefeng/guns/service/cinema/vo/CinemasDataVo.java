package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CinemasDataVo implements Serializable {

    private static final long serialVersionUID = -8459086387672535508L;

    private Integer uuid;

    private String cinemaName;

    private String cinemaAddress;

    private String address;

    private Integer minimumPrice;
}
