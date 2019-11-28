package com.stylefeng.guns.user.common.persistence.service;

import com.stylefeng.guns.service.user.MtimeUserService;
import com.stylefeng.guns.service.user.vo.BaseVo;
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

    @Override
    public BaseVo login(String userName, String password) {
        return null;
    }

    @Override
    public BaseVo register(String userName, String password, String email, String mobile, String adress) {
        return null;
    }
}
