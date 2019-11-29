package com.stylefeng.guns.service.film.vo;

import lombok.Data;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 20:40
 */

@Data
public class FilmVo {
    private Integer status;
    private String msg;

    public static FilmVo fail(Integer status, String msg){
        FilmVo filmVo = new FilmVo();
        filmVo.setStatus(status);
        filmVo.setMsg(msg);
        return filmVo;
    }
}
