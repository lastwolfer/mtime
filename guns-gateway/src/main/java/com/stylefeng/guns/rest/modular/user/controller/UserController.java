package com.stylefeng.guns.rest.modular.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.service.user.MtimeUserService;
import com.stylefeng.guns.service.user.beans.UserInfo;
import com.stylefeng.guns.service.user.beans.UserRegister;
import com.stylefeng.guns.service.user.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
public class UserController {

    @Reference(interfaceClass = MtimeUserService.class,  check = false)
    MtimeUserService mtimeUserService;
    @Autowired
    RedisTemplate redisTemplate;

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

    //获取用户信息
    @RequestMapping("getUserInfo")
    public BaseVo getUserInfo(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorization = request.getHeader("Authorization");
        if(authorization==null){
            return new BaseVo(1, "查询失败，用户尚未登陆", null);
        }
        String authToken = authorization.substring(7);
        UserInfo userInfo = (UserInfo) redisTemplate.opsForValue().get(authToken);
        if( userInfo != null ){
            return new BaseVo(0, null, userInfo);
        }else {
            return new BaseVo(999, "系统出现异常,请联系管理员", null);
        }
    }

    //用户注册
    @RequestMapping("register")
    public BaseVo registerUser(UserRegister userRegister){
        BaseVo result = mtimeUserService.register(userRegister);
        return result;
    }

    //修改用户信息
    @RequestMapping("updateUserInfo")
    public BaseVo updateUserInfo(UserInfo userInfo){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorization = request.getHeader("Authorization");
        String authToken = authorization.substring(7);
        UserInfo update = mtimeUserService.updateUserInfo(userInfo);
        if(update == null){
            return new BaseVo(1, "用户资料修改失败", null);
        }else{
            redisTemplate.opsForValue().set(authToken, update);
            return new BaseVo(0, null, update);
        }
    }
    @RequestMapping(value = "/logout")
    public BaseVo loginOut(){
        BaseVo baseVo = new BaseVo();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorization = request.getHeader("Authorization");
        String authToken = authorization.substring(7);
        if(authToken == null){
            baseVo.setStatus(1);
            baseVo.setMsg("退出失败，用户尚未登陆");
            return baseVo;
        }
        boolean b = mtimeUserService.loginOut(authToken);
        System.out.println(authToken);
        System.out.println(b);
        if(b == true ) {
            baseVo.setStatus(0);
            baseVo.setMsg("成功退出");
            return baseVo;
        }
        return new BaseVo(0, "成功退出", null);
    }
}
