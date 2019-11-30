package com.stylefeng.guns.service.film.vo;

import lombok.Data;

@Data
public class BaseRespVo<T> {
<<<<<<< HEAD
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
    private String nowPage;
    private int status=0;


=======

    String mes;
    T data;
    private String msg="";
    private String totalPage="";
    private String imgPre;
    private String nowPage="";
    private int status=0;

>>>>>>> bf83aada6de4d69c1abe586f4eed591aa339018d
    public static BaseRespVo ok(Object o){
        BaseRespVo<Object> objectBaseRespVo = new BaseRespVo<>();

        objectBaseRespVo.setStatus(0);
        objectBaseRespVo.setData(o);
        return objectBaseRespVo;
    }

}
