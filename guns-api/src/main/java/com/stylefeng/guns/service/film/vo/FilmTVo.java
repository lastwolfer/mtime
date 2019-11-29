package com.stylefeng.guns.service.film.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FilmTVo implements Serializable {

    private static final long serialVersionUID = -6997849592405539586L;
    /**
     * filmCats :
     * score : 8.0
     * expectNum : 234235
     * boxNum : 123421
     * filmId : 13
     * filmLength :
     * filmType : 0
     * showTime : 2018-07-12
     * filmName : 素人特工
     * imgAddress : c0bec212d759ad52f22bbb280e551c065000875.jpg
     * filmScore : 8.0
     */
    private String filmCats;
    private String score;
    private int expectNum;
    private int boxNum;
    private Integer filmId;
    private String filmLength;
    private int filmType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone =  "GMT+8")
    private Date showTime;
    private String filmName;
    private String imgAddress;
    private String filmScore;
}
