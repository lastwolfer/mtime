package com.stylefeng.guns.user.common.persistence.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.service.user.beans.UserInfo;
import com.stylefeng.guns.service.user.beans.UserRegister;
import com.stylefeng.guns.user.common.persistence.dao.MtimeUserTMapper;
import com.stylefeng.guns.user.common.persistence.model.MtimeUserT;
import com.stylefeng.guns.service.user.MtimeUserService;
import com.stylefeng.guns.service.user.vo.BaseVo;
import com.stylefeng.guns.user.modular.auth.util.JwtTokenUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public int login(String userName, String password) {
        Map<String,Object> map = new HashMap<>();
        map.put("user_name",userName);
        String encrypt = MD5Util.encrypt(password);
        map.put("user_pwd", encrypt);
        List<MtimeUserT> userList = mtimeUserTMapper.selectByMap(map);
        if(userList != null){
            return 1;
        }else{
            return -1;
        }
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
        }
        return userInfo;
    }


}
