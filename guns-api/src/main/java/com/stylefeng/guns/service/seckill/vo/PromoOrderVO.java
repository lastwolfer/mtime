package com.stylefeng.guns.service.seckill.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: jia.xue
 * @create: 2019-08-08 11:17
 * @Description
 **/
@Data
public class PromoOrderVO implements Serializable {
    private static final long serialVersionUID = -465563487936533027L;

    private String uuid;
    private Integer userId;
    private Integer cinemaId;
    private String exchangeCode;
    private Date affectedStartTime;
    private Date affectedEndTime;
    private Integer amount;
    private double price;
    private Date createTime;

}
