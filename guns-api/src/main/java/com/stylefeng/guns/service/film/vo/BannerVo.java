package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BannerVo implements Serializable {

    private static final long serialVersionUID = -2790064542382061017L;
    /**
     * bannerId : 2
     * bannerUrl : www.meetingshop.cn
     * bannerAddress : banner1.png
     */
    private Integer bannerId;
    private String bannerUrl;
    private String bannerAddress;

}
