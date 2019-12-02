package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseRespVo<T> implements Serializable {

    T data;
    /**
     * msg :
     * totalPage :
     * imgPre : http://img.meetingshop.cn/
     * nowPage :
     * status : 0
     */
    private String msg="";
    private String totalPage="";
    private String imgPre;
    private String nowPage="";
    private int status=0;


    public static BaseRespVo ok(Object o){
        BaseRespVo<Object> objectBaseRespVo = new BaseRespVo<>();
        objectBaseRespVo.setStatus(0);
        objectBaseRespVo.setData(o);
        objectBaseRespVo.setStatus(0);
        return objectBaseRespVo;
    }

    public static BaseRespVo fail(Integer status,String msg){
        BaseRespVo<Object> objectBaseRespVo = new BaseRespVo<>();
        objectBaseRespVo.setStatus(status);
        objectBaseRespVo.setMsg(msg);
        return objectBaseRespVo;
    }

}
