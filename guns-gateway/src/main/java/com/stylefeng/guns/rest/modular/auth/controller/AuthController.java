package com.stylefeng.guns.rest.modular.auth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.rest.common.exception.BizExceptionEnum;
import com.stylefeng.guns.rest.common.persistence.model.User;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthRequest;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthResponse;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.stylefeng.guns.rest.modular.auth.validator.IReqValidator;
import com.stylefeng.guns.service.cinema.vo.RespVo;
import com.stylefeng.guns.service.user.UserService;
import com.stylefeng.guns.service.user.vo.MtimeUserVO;
import com.stylefeng.guns.service.user.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 登录的单点鉴权
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:22
 */
@RestController
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisTemplate redisTemplate;
    @Reference(check = false)
    UserService userService;

    @Resource(name = "simpleValidator")
    private IReqValidator reqValidator;

    @RequestMapping(value = "${jwt.auth-path}")
    public ResponseEntity<?> createAuthenticationToken(AuthRequest authRequest) {
        UserVo user = new UserVo();
        //BeanUtils.copyProperties(authRequest,user);
        user.setUsername(authRequest.getUserName());
        user.setPassword(authRequest.getPassword());
        //认证
        RespVo login = userService.login(user);
        if(login.getStatus() == 1){
            //账号错误
            throw new GunsException(BizExceptionEnum.AUTH_REQUEST_ERROR);
        } else if(login.getStatus() == 999){
            //服务器错误
            throw new GunsException(BizExceptionEnum.AUTH_REQUEST_ERROR);
        }
        //认证成功，生成token
        final String randomKey = jwtTokenUtil.getRandomKey();
        final String token = jwtTokenUtil.generateToken(authRequest.getUserName(), randomKey);
        //取出data里的user信息
        MtimeUserVO mtimeUserVO = (MtimeUserVO) login.getData();
        //存入redis中，并设置过期时间
        redisTemplate.opsForValue().set(token,mtimeUserVO);
        redisTemplate.expire(token,10*60, TimeUnit.SECONDS);
        MtimeUserVO o = (MtimeUserVO) redisTemplate.opsForValue().get(token);
        System.out.println(o);
        //大功告成，返回token给前端保存
        return ResponseEntity.ok(new AuthResponse(token, randomKey));

    }
}
