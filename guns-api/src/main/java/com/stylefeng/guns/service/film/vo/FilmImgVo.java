package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 22:18
 */

@Data
public class FilmImgVo implements Serializable {
    private static final long serialVersionUID = 5191571675369199170L;
    private String mainImg;
    private String img01;
    private String img02;
    private String img03;
    private String img04;
}
