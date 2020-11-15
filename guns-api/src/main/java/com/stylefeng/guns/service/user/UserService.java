package com.stylefeng.guns.service.user;

import com.stylefeng.guns.service.cinema.vo.RespVo;
import com.stylefeng.guns.service.user.vo.UserVo;

public interface UserService {
     RespVo register(UserVo userVo);
     RespVo login(UserVo userVo);
}
