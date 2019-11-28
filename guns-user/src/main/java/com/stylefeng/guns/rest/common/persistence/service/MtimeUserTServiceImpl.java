package com.stylefeng.guns.rest.common.persistence.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeUserT;
import com.stylefeng.guns.service.user.MtimeUserService;
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
    public int login(String userName, String password) {
        Map<String,Object> map = new HashMap<>();
        map.put("userName",userName);
        map.put("userPwd", password);
        List<MtimeUserT> userList = mtimeUserTMapper.selectByMap(map);
        return 0;
    }

    @Override
    public BaseVo register(String userName, String password, String email, String mobile, String adress) {
        return null;
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
