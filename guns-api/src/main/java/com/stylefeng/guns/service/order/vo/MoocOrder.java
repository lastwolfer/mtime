package com.stylefeng.guns.service.order.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单信息表
 * </p>
 *
 * @author pandsx
 * @since 2019-12-01
 */
@Data
public class MoocOrder implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键编号
     */
    private String uuid;
    /**
     * 影院编号
     */
    private Integer cinemaId;
    /**
     * 放映场次编号
     */
    private Integer fieldId;
    /**
     * 电影编号
     */
    private Integer filmId;
    /**
     * 已售座位编号
     */
    private String seatsIds;
    /**
     * 已售座位名称
     */
    private String seatsName;
    /**
     * 影片售价
     */
    private Double filmPrice;
    /**
     * 订单总金额
     */
    private Double orderPrice;
    /**
     * 下单时间
     */
    private Date orderTime;
    /**
     * 下单人
     */
    private Integer orderUser;
    /**
     * 0-待支付,1-已支付,2-已关闭
     */
    private Integer orderStatus;

}
