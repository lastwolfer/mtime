package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RespVo implements Serializable {
    private static final long serialVersionUID = -5213178114075657849L;
    private Integer status;

    private Integer nowPage;

    private Integer totalPage;

    private Object data;

    private String msg;
    private String imgPre;
}
