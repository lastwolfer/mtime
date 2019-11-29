package com.stylefeng.guns.user.common.persistence.dao;

import com.stylefeng.guns.service.user.beans.UserInfo;
import com.stylefeng.guns.user.common.persistence.model.MtimeUserT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author AiTeboy
 * @since 2019-11-28
 */
public interface MtimeUserTMapper extends BaseMapper<MtimeUserT> {

    int updateUserInfo(@Param("user") UserInfo userInfo);
}
