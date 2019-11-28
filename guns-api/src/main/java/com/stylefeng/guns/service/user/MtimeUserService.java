package com.stylefeng.guns.service.user;

import com.stylefeng.guns.service.user.beans.UserRegister;
import com.stylefeng.guns.service.user.vo.BaseVo;

public interface MtimeUserService {

    //用户登录
    int login(String userName, String password);

    //用户名验证
    int usernameCheck(String username);

}