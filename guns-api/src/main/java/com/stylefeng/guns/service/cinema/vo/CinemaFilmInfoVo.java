package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CinemaFilmInfoVo implements Serializable {

    private static final long serialVersionUID = -6513559157043559682L;

    private String filmId;

    private String filmName;

    private String filmType;

    private String imgAddress;

    private String filmCats;

    private String filmLength;
}
