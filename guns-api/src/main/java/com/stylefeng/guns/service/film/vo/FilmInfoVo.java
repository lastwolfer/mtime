package com.stylefeng.guns.service.film.vo;

import lombok.Data;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 20:56
 */

@Data
public class FilmInfoVo {
    private Integer filmId;
    private Integer filmType;
    private String imgAddress;
    private String filmName;
    private String filmScore;
}
