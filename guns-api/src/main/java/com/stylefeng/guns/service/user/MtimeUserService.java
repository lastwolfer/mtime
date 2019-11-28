package com.stylefeng.guns.service.user;

import com.stylefeng.guns.service.user.beans.UserRegister;
import com.stylefeng.guns.service.user.vo.BaseVo;

public interface MtimeUserService {

    //用户登录
    BaseVo login(UserRegister userRegister);

    //用户注册
    BaseVo register(UserRegister userRegister);
}
