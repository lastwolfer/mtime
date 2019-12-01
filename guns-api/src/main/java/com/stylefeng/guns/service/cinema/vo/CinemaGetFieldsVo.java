package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CinemaGetFieldsVo implements Serializable {
    private static final long serialVersionUID =123123;
    private CinemaInfoVo cinemaInfo;
    private List<CFilmVo> filmList;

}
