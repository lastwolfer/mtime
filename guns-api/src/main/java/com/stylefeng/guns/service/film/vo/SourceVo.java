package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SourceVo implements Serializable {

    private static final long serialVersionUID = 2479731427214085877L;
    /**
     * sourceId : 1
     * active : false
     * sourceName : 大陆
     */
    private String sourceId;
    private boolean active=false;
    private String sourceName;
}
