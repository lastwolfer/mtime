package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FilmFieldsVo implements Serializable {
    private static final long serialVersionUID = 897979;
    private Integer fieldId;
    private String hallName;
    private String language;
    private Integer price;
    private String beginTime;
    private String endTime;

}
