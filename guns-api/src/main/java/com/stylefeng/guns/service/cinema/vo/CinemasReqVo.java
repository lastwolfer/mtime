package com.stylefeng.guns.service.cinema.vo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@Data
public class CinemasReqVo implements Serializable {

    private static final long serialVersionUID = -1399799479105469299L;

    private Integer brandId = 99;

    private Integer hallType = 99;

    private Integer districtId = 99;

    private Integer areaId = 99;

    private Integer pageSize = 12;

    private Integer nowPage = 1;
}
