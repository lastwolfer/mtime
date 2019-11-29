package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class HallVo implements Serializable {
    private static final long serialVersionUID = -800524931195896998L;
    private Integer halltypeId;
    private String halltypeName;
    //是否选中
    private boolean active;
}
