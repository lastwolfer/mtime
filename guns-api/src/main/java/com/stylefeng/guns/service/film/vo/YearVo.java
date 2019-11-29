package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class YearVo implements Serializable {

    private static final long serialVersionUID = 6180295173671861853L;
    /**
     * active : false
     * yearName : 更早
     * yearId : 1
     */
    private boolean active=false;
    private String yearName;
    private String yearId;
}
