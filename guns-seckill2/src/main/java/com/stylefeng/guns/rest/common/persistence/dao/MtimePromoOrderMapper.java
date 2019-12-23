package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimePromoOrder;
import com.baomidou.mybatisplus.mapper.BaseMapper;import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pandax
 * @since 2019-12-04
 */
public interface MtimePromoOrderMapper extends BaseMapper<MtimePromoOrder> {

    void insertOrder(@Param("mtimePromoOrder")MtimePromoOrder mtimePromoOrder);
}
