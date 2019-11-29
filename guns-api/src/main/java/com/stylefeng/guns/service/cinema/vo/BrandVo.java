package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BrandVo implements Serializable {
    private static final long serialVersionUID = -7204240812915578573L;
    private Integer brandId;
    private String brandName;
    //是否选中
    private boolean active;
}
