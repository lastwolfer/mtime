package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RespVo implements Serializable {

    private static final long serialVersionUID = -3735237568748762549L;

    private Integer status;

    private Integer nowPage;

//    private Long totalPage;

    private Object data;

    private String msg;

    private String imgPre;

    public RespVo() {
    }

    public RespVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private Integer totalPage;
}
