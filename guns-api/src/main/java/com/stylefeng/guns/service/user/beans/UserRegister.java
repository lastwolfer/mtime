package com.stylefeng.guns.service.user.beans;

import lombok.Data;

/**
 * 用户时候接收的类
 */
@Data
public class UserRegister {
    private String userName;
    private String password;
    private String mobile;
    private String email;
    private String address;
}
