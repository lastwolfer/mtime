package com.stylefeng.guns.cinema.controller;

import lombok.Data;

@Data
public class FilmFieldsVo {
    private Integer fieldId;
    private String hallName;
    private String language;
    private Integer price;
    private String beginTime;
    private String endTime;

}
