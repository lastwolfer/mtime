package com.stylefeng.guns.service.user.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVo implements Serializable {
    private static final long serialVersionUID = -363174540567259499L;
    private String username;
    private String password;
    private String email;
    private String mobile;
    private String address;
}
