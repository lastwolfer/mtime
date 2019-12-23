package com.stylefeng.guns.rest.common.persistence.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeUserT;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.stylefeng.guns.service.user.MtimeUserService;
import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.service.user.beans.UserInfo;

import com.stylefeng.guns.service.user.beans.UserRegister;

import com.stylefeng.guns.service.user.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author AiTeboy
 * @since 2019-11-28
 */
@Component
@Service(interfaceClass = MtimeUserService.class)
public class MtimeUserTServiceImpl implements MtimeUserService {

    @Autowired
    MtimeUserTMapper mtimeUserTMapper;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    RedisTemplate redisTemplate;



    @Override
    public BaseVo register(UserRegister userRegister) {
        BaseVo baseVo = new BaseVo();
        //对MtimeUserT类对象赋值
        MtimeUserT mtimeUserT = new MtimeUserT();
        mtimeUserT.setUserName(userRegister.getUsername());
        mtimeUserT.setUserPwd(userRegister.getPassword());
        mtimeUserT.setUserPhone(userRegister.getMobile());
        mtimeUserT.setEmail(userRegister.getEmail());
        mtimeUserT.setAddress(userRegister.getAddress());
        //查询用户是否存在，根据用户名查找
        EntityWrapper<MtimeUserT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_name", userRegister.getUsername());
        Integer integer = mtimeUserTMapper.selectCount(entityWrapper);
        if (integer > 0) {//已经存在用户
            baseVo.setStatus(1);
            baseVo.setMsg("已经存在用户");
            return baseVo;
        } else if (integer == 0) {//注册成功
            //先密码加密再插入数据
            String encrypt = MD5Util.encrypt(mtimeUserT.getUserPwd());
            mtimeUserT.setUserPwd(encrypt);
            Integer insert = mtimeUserTMapper.insert(mtimeUserT);
            if (insert == 1) {
                baseVo.setMsg("注册成功");
                baseVo.setStatus(0);
                return baseVo;
            }
        }
        //其他情况，系统出现异常，请联系管理员
        baseVo.setStatus(999);
        baseVo.setMsg("系统出现异常，请联系管理员");
        return baseVo;
    }

    @Override
    public UserInfo login(String userName, String password) {
        Map<String,Object> map = new HashMap<>();
        map.put("user_name",userName);
        String encrypt = MD5Util.encrypt(password);
        map.put("user_pwd", encrypt);
        List<MtimeUserT> userList = mtimeUserTMapper.selectByMap(map);
        if( userList.size() != 0 ){
            MtimeUserT mtimeUserT = userList.get(0);
            UserInfo userInfo = userInfoTransfer(mtimeUserT);
            return userInfo;
        }else{
            return null;
        }

    }

    /**
     *
     * @return 是否退出判断值
     */
    @Override
    public boolean loginOut(String authorization) {
        System.out.println(authorization);
        Boolean delete = redisTemplate.delete( authorization);
        return delete;
    }

    @Override
    public int usernameCheck(String username) {
        Map<String,Object> map = new HashMap<>();
        map.put("userName", username);
        List<MtimeUserT> userList = mtimeUserTMapper.selectByMap(map);
        if( userList!=null ){
            return 1;
        }else{
            return -1;
        }
    }

    @Override
    public UserInfo getUserInfo(String authToken) {
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        Map<String,Object> map = new HashMap<>();
        map.put("user_name", username);
        List<MtimeUserT> userList = mtimeUserTMapper.selectByMap(map);
        UserInfo userInfo = new UserInfo();
        if( userList != null ) {
            MtimeUserT user = userList.get(0);
            userInfo = userInfoTransfer(user);
        }
        return userInfo;
    }

    @Override
    public UserInfo updateUserInfo(UserInfo userInfo) {
        int updateStatus = mtimeUserTMapper.updateUserInfo(userInfo);
        if(updateStatus != -1){
            MtimeUserT mtimeUserT = mtimeUserTMapper.selectById(userInfo.getUuid());
            userInfo.setHeadAddress(mtimeUserT.getHeadUrl());
            userInfo.setCreateTime(mtimeUserT.getBeginTime());
            userInfo.setUpdateTime(mtimeUserT.getUpdateTime());
            return userInfo;
        }else{
            return null;
        }
    }

    public UserInfo userInfoTransfer(MtimeUserT user){
        UserInfo userInfo = new UserInfo();
        userInfo.setUuid(user.getUuid());
        userInfo.setUsername(user.getUserName());
        userInfo.setNickname(user.getNickName());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getUserPhone());
        userInfo.setSex(user.getUserSex());
        userInfo.setBirthday(user.getBirthday());
        userInfo.setLifeState(user.getLifeState());
        userInfo.setBiography(user.getBiography());
        userInfo.setAddress(user.getAddress());
        userInfo.setHeadAddress(user.getHeadUrl());
        userInfo.setCreateTime(user.getBeginTime());
        userInfo.setUpdateTime(user.getUpdateTime());
        return userInfo;
    }


}
