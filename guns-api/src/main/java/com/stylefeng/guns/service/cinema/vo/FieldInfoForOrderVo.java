package com.stylefeng.guns.service.cinema.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FieldInfoForOrderVo implements Serializable {
    private Integer uuid;
    private Integer cinemaId;
    private Integer filmId;
    private Integer price;
    private String beginTime;
}
