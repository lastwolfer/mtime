package com.stylefeng.guns.user.common.persistence.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.service.user.MtimeUserService;
import com.alibaba.dubbo.config.annotation.Service;

import com.stylefeng.guns.service.user.beans.UserRegister;
import com.stylefeng.guns.user.common.persistence.dao.MtimeUserTMapper;
import com.stylefeng.guns.user.common.persistence.model.MtimeUserT;

import com.stylefeng.guns.service.user.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public int login(String userName, String password) {
        Map<String,Object> map = new HashMap<>();
        map.put("userName",userName);
        map.put("userPwd", password);
        List<MtimeUserT> userList = mtimeUserTMapper.selectByMap(map);
        return 0;
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


}
