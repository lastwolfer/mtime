package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CatVo implements Serializable {

    private static final long serialVersionUID = 3175155554118458343L;
    /**
     * areaId : 1
     * areaName : 朝阳区
     * active : false
     */
    private int areaId;
    private String areaName;
    private boolean active=false;
}
