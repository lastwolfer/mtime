package com.stylefeng.guns.cinema.controller;

import lombok.Data;

@Data
public class RespVo {

    private Integer status;

    private Integer nowPage;

    private Integer totalPage;

    private Object data;

}