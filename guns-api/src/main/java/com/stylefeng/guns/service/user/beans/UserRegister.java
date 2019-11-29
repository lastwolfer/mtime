package com.stylefeng.guns.service.user.beans;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户时候接收的类
 */
@Data
public class UserRegister implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String mobile;
    private String email;
    private String address;
}
