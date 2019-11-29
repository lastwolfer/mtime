package com.stylefeng.guns.user.modular.auth.validator.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.user.common.persistence.dao.UserMapper;
import com.stylefeng.guns.user.common.persistence.model.User;
import com.stylefeng.guns.user.modular.auth.validator.IReqValidator;
import com.stylefeng.guns.user.modular.auth.validator.dto.Credence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
<<<<<<< HEAD:guns-film/src/main/java/com/stylefeng/guns/rest/modular/auth/validator/impl/DbValidator.java
=======

>>>>>>> c00ab290aca00348b46d78c5ac8bff7cca3a9219:guns-user/src/main/java/com/stylefeng/guns/user/modular/auth/validator/impl/DbValidator.java

/**
 * 账号密码验证
 *
 * @author fengshuonan
 * @date 2017-08-23 12:34
 */
@Service
public class DbValidator implements IReqValidator {

    @Autowired
    UserMapper userMapper;

    @Override
    public boolean validate(Credence credence) {
        List<User> users = userMapper.selectList(new EntityWrapper<User>().eq("userName", credence.getCredenceName()));
        if (users != null && users.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
