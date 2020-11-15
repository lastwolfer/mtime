package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FilmVo implements Serializable {

    private static final long serialVersionUID = 5790046328761569923L;

    private Integer filmId;//主键编号uuid
    private String filmName;//影片名称
    private Integer filmType;//片源类型: 0-2D,1-3D,2-3DIMAX,4-无
    private String imgAddress;//影片主图地址
    private String filmScore;//影片评分
    private Integer expectNum;//影片预售数量filmPresalenum
    private Integer boxNum;//影片票房：每日更新，以万为单位(filmBoxOffice)
    private Integer filmSource;//影片片源，参照片源字典表
    private String filmCats;//影片分类，参照分类表,多个分类以#分割
    private Integer filmArea;//影片区域，参照区域表
    private Integer filmDate;//影片上映年代，参照年代表
    private Date showTime;//影片上映时间filmTime
    private Integer filmStatus;//影片状态,1-正在热映，2-即将上映，3-经典影片
    //前端新增一个score
    private String score;
}
