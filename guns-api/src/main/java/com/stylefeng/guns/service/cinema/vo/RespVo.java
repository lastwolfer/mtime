package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RespVo<T> implements Serializable {

    private static final long serialVersionUID = -3735237568748762549L;

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


    //成功时返回
    public static RespVo ok(Object o){
        RespVo objectBaseRespVo = new RespVo();
        objectBaseRespVo.setStatus(0);
        objectBaseRespVo.setData(o);
        objectBaseRespVo.setStatus(0);
        return objectBaseRespVo;
    }
    //失败时返回
    public static RespVo fail(Integer status,String msg){
        RespVo objectBaseRespVo = new RespVo();
        objectBaseRespVo.setStatus(status);
        objectBaseRespVo.setMsg(msg);
        return objectBaseRespVo;
    }

    public RespVo() {
    }

    public RespVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
