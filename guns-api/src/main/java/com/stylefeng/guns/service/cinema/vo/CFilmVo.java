package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CFilmVo implements Serializable {
    private static final long serialVersionUID = 121212;
    private String actors;
    private String filmCats;
    private Integer filmId;
    private String filmLength;
    private String filmType;
    private String imgAddress;
    private List<FilmFieldsVo> filmFields;
}
