package com.stylefeng.guns.rest.modular.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.service.user.MtimeUserService;
import com.stylefeng.guns.service.user.vo.BaseVo;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Reference(interfaceClass = MtimeUserService.class)
    MtimeUserService mtimeUserService;

    //用户名验证
    @RequestMapping("check")
    BaseVo usernameCheck(String username){
        int i = mtimeUserService.usernameCheck(username);
        if( i == -1){
            return new BaseVo(0, "用户名不存在", null);
        }else if( i == 1){
            return new BaseVo(1, "用户已经注册", null);
        }else{
            return new BaseVo(999, "系统出现异常", null);
        }

    }
}