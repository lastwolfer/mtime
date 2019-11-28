package com.stylefeng.guns.service.film.vo;

import lombok.Data;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 17:40
 */

@Data
public class FilmDetailVo extends FilmVo{
    private String filmId;
    private String filmName;
    private String filmEnName;
    private String filmAddress;
    private String score;
    private String scoreNum;
    private String totalBox;
    private String info01;
    private String info02;
    private String info03;
    private InfoRequestVO info04;
    private FilmImgVo imgVO;
    private String imgPre;

}
