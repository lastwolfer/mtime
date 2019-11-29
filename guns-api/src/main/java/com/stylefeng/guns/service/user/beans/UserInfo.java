package com.stylefeng.guns.service.user.beans;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserInfo implements Serializable {

    private Integer uuid;

    private String username;

    private String nickname;

    private String email;

    private String phone;

    private Integer sex;

    private String birthday;

    private Integer lifeState;

    private String biography;

    private String address;

    private String headAddress;

    private Date createTime;

    private Date updateTime;


}
