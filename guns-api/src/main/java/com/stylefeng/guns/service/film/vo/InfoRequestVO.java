package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 21:53
 */

@Data
public class InfoRequestVO implements Serializable {
    private static final long serialVersionUID = 6074508088322235659L;
    private String biography;
    private ActorsInfo actors;
    private FilmImgVo imgVO;
    private String filmId;
}
