package com.stylefeng.guns.rest.modular.auth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthRequest;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.stylefeng.guns.service.user.MtimeUserService;
import com.stylefeng.guns.service.user.beans.UserInfo;
import com.stylefeng.guns.service.user.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 请求验证的
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:22
 */
@RestController
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Reference(interfaceClass = MtimeUserService.class,check = false)
    MtimeUserService mtimeUserService;
    @Autowired
    RedisTemplate redisTemplate;

//    @Resource(name = "simpleValidator")
//    private IReqValidator reqValidator;

    @RequestMapping(value = "${jwt.auth-path}")
    public BaseVo createAuthenticationToken(AuthRequest authRequest) {

        UserInfo loginUser = mtimeUserService.login(authRequest.getUserName(), authRequest.getPassword());
        System.out.println(loginUser);
        if (loginUser != null) {
            /*登陆成功，生成token*/
            final String randomKey = jwtTokenUtil.getRandomKey();
            final String token = jwtTokenUtil.generateToken(authRequest.getUserName(), randomKey);
            /*把用户信息存入redis*/
            redisTemplate.opsForValue().set(token, loginUser, 30*60, TimeUnit.SECONDS);
            Map<String,Object> map = new HashMap<>();
            map.put("randomKey", randomKey);
            map.put("token", token);
            return new BaseVo(0, null, map);
        } else {
            return new BaseVo(1, "用户名或密码错误", null);
        }
    }
}
