package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author pandsx
 * @since 2019-12-01
 */
public interface MoocOrderTMapper extends BaseMapper<MoocOrderT> {

    void cancelOrder(@Param("orderId") String orderId);
}
