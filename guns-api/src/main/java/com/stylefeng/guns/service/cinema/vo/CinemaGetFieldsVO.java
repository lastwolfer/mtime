package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CinemaGetFieldsVO implements Serializable {
    private static final long serialVersionUID =123123;
    private Integer cinemaId;
    private String cinemaAdress;
    private String cinemaName;
    private String cinemaPhone;
    private String imgUrl;
    private List<CFilmVo> filmList;
}
