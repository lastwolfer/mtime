package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FieldInfoForOrderVo implements Serializable {
    private Integer uuid;
    private Integer cinemaId;
    private Integer filmId;
    private Integer price;
}
