package com.stylefeng.guns.service.user.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author jay
 * @since 2019-11-29
 */
@Data
public class MtimeUserVO implements Serializable{
    private static final long serialVersionUID = 108902976596906910L;
    /**
     * 主键编号
     */
    private Integer uuid;
    /**
     * 用户账号
     */
    private String userName;
    /**
     * 用户密码
     */
    private String userPwd;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户性别 0-男，1-女
     */
    private Integer userSex;
    /**
     * 出生日期
     */
    private String birthday;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户手机号
     */
    private String userPhone;
    /**
     * 用户住址
     */
    private String address;
    /**
     * 头像URL
     */
    private String headUrl;
    /**
     * 个人介绍
     */
    private String biography;
    /**
     * 生活状态 0-单身，1-热恋中，2-已婚，3-为人父母
     */
    private Integer lifeState;
    /**
     * 创建时间
     */
    private Date beginTime;
    /**
     * 修改时间
     */
    private Date updateTime;

    public MtimeUserVO() {
    }

    public MtimeUserVO(Integer uuid, String userName, String userPwd, String nickName, Integer userSex, String birthday, String email, String userPhone, String address, String headUrl, String biography, Integer lifeState, Date beginTime, Date updateTime) {
        this.uuid = uuid;
        this.userName = userName;
        this.userPwd = userPwd;
        this.nickName = nickName;
        this.userSex = userSex;
        this.birthday = birthday;
        this.email = email;
        this.userPhone = userPhone;
        this.address = address;
        this.headUrl = headUrl;
        this.biography = biography;
        this.lifeState = lifeState;
        this.beginTime = beginTime;
        this.updateTime = updateTime;
    }
}
