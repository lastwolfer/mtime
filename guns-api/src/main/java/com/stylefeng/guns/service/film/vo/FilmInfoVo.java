package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 20:56
 */

@Data
public class FilmInfoVo implements Serializable {
    private static final long serialVersionUID = 8146730898358970496L;
    private Integer filmId;
    private String filmType;
    private String imgAddress;
    private String filmName;
    private String filmScore;
}
