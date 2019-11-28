package com.stylefeng.guns.cinema.controller;

import com.stylefeng.guns.cinema.persistence.model.MtimeHallFilmInfoT;
import lombok.Data;

import java.util.List;

@Data
public class FilmVo {
    private String actors;
    private String filmCats;
    private Integer filmId;
    private String filmLength;
    private String filmType;
    private String imgAddress;
    private List<FilmFieldsVo> filmFields;
}
