package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimePromoStock;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jay
 * @since 2019-12-03
 */
public interface MtimePromoStockMapper extends BaseMapper<MtimePromoStock> {
    int updateStockByPromoId(@Param("promoId") Integer promoId, @Param("amount") Integer amount);
}
