package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeStockLog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jay
 * @since 2019-12-05
 */
public interface MtimeStockLogMapper extends BaseMapper<MtimeStockLog> {

    int updateStatusById(@Param("uuid") String uuid, @Param("status") Integer status );
}
