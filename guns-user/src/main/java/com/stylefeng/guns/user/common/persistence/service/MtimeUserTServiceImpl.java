package com.stylefeng.guns.user.common.persistence.service;

import com.stylefeng.guns.service.user.MtimeUserService;
import com.stylefeng.guns.service.user.beans.UserRegister;
import com.stylefeng.guns.service.user.vo.BaseVo;
import com.stylefeng.guns.user.common.persistence.dao.MtimeUserTMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author AiTeboy
 * @since 2019-11-28
 */
@Service
@com.alibaba.dubbo.config.annotation.Service(interfaceClass = MtimeUserService.class)
public class MtimeUserTServiceImpl implements MtimeUserService {

    @Autowired
    MtimeUserTMapper mtimeUserTMapper;

    @Override
    public BaseVo login(UserRegister userRegister) {
        return null;
    }

    @Override
    public BaseVo register(UserRegister userRegister) {

        return null;
    }
}
