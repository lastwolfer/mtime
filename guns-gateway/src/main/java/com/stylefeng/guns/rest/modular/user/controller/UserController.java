package com.stylefeng.guns.rest.modular.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.service.user.MtimeUserService;
import com.stylefeng.guns.service.user.beans.UserInfo;
import com.stylefeng.guns.service.user.beans.UserRegister;
import com.stylefeng.guns.service.user.vo.BaseVo;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
public class UserController {

    @Reference(interfaceClass = MtimeUserService.class,check = false)
    MtimeUserService mtimeUserService;

    //用户名验证
    @RequestMapping("check")
    public BaseVo usernameCheck(String username){
        int i = mtimeUserService.usernameCheck(username);
        if( i == -1){
            return new BaseVo(0, "用户名不存在", null);
        }else if( i == 1){
            return new BaseVo(1, "用户已经注册", null);
        }else{
            return new BaseVo(999, "系统出现异常", null);
        }
    }

    @RequestMapping("getUserInfo")
    BaseVo getUserInfo(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorization = request.getHeader("Authorization");
        if(authorization==null){
            return new BaseVo(1, "查询失败，用户尚未登陆", null);
        }
        String authToken = authorization.substring(7);
        UserInfo userInfo = mtimeUserService.getUserInfo(authToken);
        if( userInfo != null ){
            return new BaseVo(0, null, userInfo);
        }else {
            return new BaseVo(999, "系统出现异常,请联系管理员", null);
        }
    }

    @RequestMapping("register")
    public BaseVo registerUser(UserRegister userRegister){
        BaseVo result = mtimeUserService.register(userRegister);
        return result;
    }
}
