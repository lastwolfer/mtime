package com.stylefeng.guns.service.promo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PromoConditionVo implements Serializable{

    private static final long serialVersionUID = 3441586326946933157L;
    //接收查询的条件参数
    private Integer barndId;
    private Integer hallType;
    private Integer areaId;
    private Integer pageSize;
    private Integer nowPage;
}
