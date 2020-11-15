package com.stylefeng.guns.rest.common.persistence.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.service.cinema.vo.RespVo;
import com.stylefeng.guns.service.user.UserService;
import com.stylefeng.guns.service.user.vo.UserVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    @Reference(check = false)
    UserService userService;

    @RequestMapping("register")
    public RespVo register(UserVo userVo){
        RespVo register = userService.register(userVo);
        return register;
    }

}
