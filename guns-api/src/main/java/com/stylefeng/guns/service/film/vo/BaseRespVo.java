package com.stylefeng.guns.service.film.vo;

import lombok.Data;

@Data
public class BaseRespVo<T> {
    String mes;
    Integer status;
    T data;
    public static BaseRespVo ok(Object o){
        BaseRespVo<Object> objectBaseRespVo = new BaseRespVo<>();
        objectBaseRespVo.setData(o);
        objectBaseRespVo.setStatus(0);
        return objectBaseRespVo;
    }
}
