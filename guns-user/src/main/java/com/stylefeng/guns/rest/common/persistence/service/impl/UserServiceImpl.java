package com.stylefeng.guns.rest.common.persistence.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeUserT;
import com.stylefeng.guns.service.cinema.vo.RespVo;
import com.stylefeng.guns.service.user.UserService;
import com.stylefeng.guns.service.user.vo.MtimeUserVO;
import com.stylefeng.guns.service.user.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    MtimeUserTMapper userMapper;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public RespVo register(UserVo userVo) {
        //校验用户是否已存在
        String username = userVo.getUsername();
        EntityWrapper<MtimeUserT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_name",username);
        Integer num = userMapper.selectCount(entityWrapper);
        if(num > 0){
            //throw new GunsException(UserExceptionEnum.ACCOUNT_REPEAT);
            RespVo respVo = new RespVo();
            respVo.setMsg("用户已存在");
            respVo.setStatus(1);
            return respVo;
        }
        //保存信息到数据库中
        //需要修改一下前端，把这五项改为必填项
        else if(num == 0) {
            //密码通过MD5加密存入数据库
            String password = MD5Util.encrypt(userVo.getPassword());
            MtimeUserT mtimeUserT = new MtimeUserT(userVo.getUsername(),
                    password,
                    userVo.getEmail(),
                    userVo.getMobile(),
                    userVo.getAddress());
            Integer insert = userMapper.insert(mtimeUserT);
            if(insert == 1) {
                RespVo respVo = new RespVo();
                respVo.setStatus(0);
                respVo.setMsg("注册成功");
                return respVo;
            }
        }
        //其他情况一律返回服务器错误
        RespVo respVo = new RespVo();
        respVo.setStatus(900);
        respVo.setMsg("服务器错误");
        return respVo;
    }

    @Override
    public RespVo login(UserVo userVo) {
        //去数据库中查询用户名和密码是否正确
        EntityWrapper<MtimeUserT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_name",userVo.getUsername());
        //密码为MD5密码
        String password = MD5Util.encrypt(userVo.getPassword());
        entityWrapper.eq("user_pwd",password);
        List<MtimeUserT> mtimeUserTS = userMapper.selectList(entityWrapper);
        if(mtimeUserTS.size() == 1){
            RespVo respVo = new RespVo();
            respVo.setStatus(0);
            MtimeUserVO mtimeUserVO = new MtimeUserVO();
            BeanUtils.copyProperties(mtimeUserTS.get(0),mtimeUserVO);
            respVo.setData(mtimeUserVO);
            respVo.setMsg("登录成功");
            return respVo;
        }
        else if(mtimeUserTS.size() == 0){
            RespVo respVo = new RespVo();
            respVo.setMsg("用户名或密码错误");
            respVo.setStatus(1);
            return respVo;
        }
        //其余情况，报错
        RespVo respVo = new RespVo();
        respVo.setStatus(999);
        respVo.setMsg("服务器错误");
        return respVo;
    }
}
