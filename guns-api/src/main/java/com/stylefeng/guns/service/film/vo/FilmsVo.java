package com.stylefeng.guns.service.film.vo;

import lombok.Data;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 17:40
 */

@Data
public class FilmsVo extends FilmVo{
    private String imgPre;
    private Integer nowPage;
    private Integer totalPage;
    private Object data;
}
