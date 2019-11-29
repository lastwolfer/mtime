package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AreaVo implements Serializable {
    private static final long serialVersionUID = 1349863485382864279L;
    private Integer areaId;
    private String areaName;
    //是否选中
    private boolean active;
}
