package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * banner信息表
 * </p>
 *
 * @author jay
 * @since 2019-11-30
 */
@Data
public class MtimeBannerVo implements Serializable {


    private static final long serialVersionUID = 4937769792309170389L;
    /**
     * 主键编号
     */
    private Integer bannerId;
    /**
     * banner图存放路径
     */
    private String bannerAddress;
    /**
     * banner点击跳转url
     */
    private String bannerUrl;
    /**
     * 是否弃用 0-失效,1-失效
     */
    private Integer isValid;

    public MtimeBannerVo(Integer bannerId, String bannerAddress, String bannerUrl) {
        this.bannerId = bannerId;
        this.bannerAddress = bannerAddress;
        this.bannerUrl = bannerUrl;
    }
}

