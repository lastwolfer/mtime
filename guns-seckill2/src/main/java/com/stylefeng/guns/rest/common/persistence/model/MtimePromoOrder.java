package com.stylefeng.guns.rest.common.persistence.model;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author pandax
 * @since 2019-12-04
 */
@TableName("mtime_promo_order")
public class MtimePromoOrder extends Model<MtimePromoOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String uuid;
    @TableField("user_id")
    private Integer userId;
    @TableField("cinema_id")
    private Integer cinemaId;
    @TableField("exchange_code")
    private String exchangeCode;
    private Integer amount;
    private BigDecimal price;
    @TableField("start_time")
    private Date startTime;
    @TableField("create_time")
    private Date createTime;
    @TableField("end_time")
    private Date endTime;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Integer cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return "MtimePromoOrder{" +
        "uuid=" + uuid +
        ", userId=" + userId +
        ", cinemaId=" + cinemaId +
        ", exchangeCode=" + exchangeCode +
        ", amount=" + amount +
        ", price=" + price +
        ", startTime=" + startTime +
        ", createTime=" + createTime +
        ", endTime=" + endTime +
        "}";
    }
}
