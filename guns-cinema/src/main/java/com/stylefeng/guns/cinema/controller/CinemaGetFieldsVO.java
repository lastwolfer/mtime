package com.stylefeng.guns.cinema.controller;

import lombok.Data;

import java.util.List;

@Data
public class CinemaGetFieldsVO {
    private Integer cinemaId;
    private String cinemaAdress;
    private String cinemaName;
    private String cinemaPhone;
    private String imgUrl;
    private List<FilmVo> filmList;
}
