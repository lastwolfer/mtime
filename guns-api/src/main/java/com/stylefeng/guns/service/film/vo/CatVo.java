package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CatVo implements Serializable {

    private static final long serialVersionUID = 3175155554118458343L;

    /**
     * catId : 1
     * catName : 爱情
     * active : false
     */
    private String catId;
    private String catName;
    private boolean active;

}
