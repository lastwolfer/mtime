package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CinemaCondition implements Serializable {
    private static final long serialVersionUID = -373746359657626118L;
    private Integer brandId;
    private Integer hallType;
    private Integer areaId;
}
