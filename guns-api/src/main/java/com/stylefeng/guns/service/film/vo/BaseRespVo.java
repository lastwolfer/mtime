package com.stylefeng.guns.service.film.vo;

import lombok.Data;

@Data
public class BaseRespVo<T> {

    String mes;
    T data;
    private String msg="";
    private String totalPage="";
    private String imgPre;
    private String nowPage="";
    private int status=0;

    public static BaseRespVo ok(Object o){
        BaseRespVo<Object> objectBaseRespVo = new BaseRespVo<>();

        objectBaseRespVo.setStatus(0);
        objectBaseRespVo.setData(o);
        return objectBaseRespVo;
    }

}
