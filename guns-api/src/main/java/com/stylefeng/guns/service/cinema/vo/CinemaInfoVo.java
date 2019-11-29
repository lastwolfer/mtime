package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CinemaInfoVo implements Serializable {

    private static final long serialVersionUID = -7689609281472259489L;

    private Integer cinemaId;

    private String imgUrl;

    private String cinemaName;

    private String cinemaAddress;

    private String cinemaPhone;
}
