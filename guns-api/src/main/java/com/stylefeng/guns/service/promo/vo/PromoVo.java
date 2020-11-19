package com.stylefeng.guns.service.promo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PromoVo implements Serializable {

    private static final long serialVersionUID = -8512812705895009973L;
        /**
         * cinemaId : 1
         * price : 10
         * cinemaName : 大地影院(顺义店)
         * description : 大地影院周年庆，等你来兑换
         * imgAddress : cinema6.jpg
         * startTime : 2019-08-15 15:35:12
         * endTime : 2019-09-19 15:35:18
         * stock : 9420
         * uuid : 1
         * cinemaAddress : 北京市顺义区华联金街购物中心
         * status : 1
         */
        private Integer cinemaId;
        private BigDecimal price;
        private String cinemaName;
        private String description;
        private String imgAddress;
        private String startTime;
        private String endTime;
        private Integer stock;
        private Integer uuid;
        private String cinemaAddress;
        private Integer status;
    }
