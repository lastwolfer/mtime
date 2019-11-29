package com.stylefeng.guns.service.user;

import com.stylefeng.guns.service.user.beans.UserInfo;
import com.stylefeng.guns.service.user.beans.UserRegister;
import com.stylefeng.guns.service.user.vo.BaseVo;

public interface MtimeUserService {

    //用户注册
    BaseVo register(UserRegister userRegister);


    //用户登录
    UserInfo login(String userName, String password);

    //用户名验证
    int usernameCheck(String username);

    //用户信息查询
    UserInfo getUserInfo(String authToken);

    //修改用户信息
    UserInfo updateUserInfo(UserInfo userInfo);
}
