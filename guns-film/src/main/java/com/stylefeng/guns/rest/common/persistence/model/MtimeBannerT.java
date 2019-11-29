package com.stylefeng.guns.rest.common.persistence.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * banner信息表
 * </p>
 *
 * @author pandax
 * @since 2019-11-28
 */
@Data
@TableName("mtime_banner_t")
public class MtimeBannerT extends Model<MtimeBannerT> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键编号
     */
    @TableId(value = "UUID", type = IdType.AUTO)
    private Integer uuid;
    /**
     * banner图存放路径
     */
    @TableField("banner_address")
    private String bannerAddress;
    /**
     * banner点击跳转url
     */
    @TableField("banner_url")
    private String bannerUrl;
    /**
     * 是否弃用 0-失效,1-失效
     */
    @TableField("is_valid")
    private Integer isValid;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
