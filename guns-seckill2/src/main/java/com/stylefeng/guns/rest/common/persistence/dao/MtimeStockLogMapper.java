package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeStockLog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pandax
 * @since 2019-12-05
 */
public interface MtimeStockLogMapper extends BaseMapper<MtimeStockLog> {

    Integer updateStatusById(@Param("uuid") String stockLogId, @Param("status") int status);
}

