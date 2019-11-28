package com.stylefeng.guns.service.user.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseVo implements Serializable {

    private  Integer status;
    private String msg;
    private Object data;

    public BaseVo(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
}
