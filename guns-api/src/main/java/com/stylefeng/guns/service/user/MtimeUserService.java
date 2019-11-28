package com.stylefeng.guns.service.user;

import com.stylefeng.guns.service.user.beans.UserRegister;
import com.stylefeng.guns.service.user.vo.BaseVo;

public interface MtimeUserService {

    BaseVo register(UserRegister userRegister);
}