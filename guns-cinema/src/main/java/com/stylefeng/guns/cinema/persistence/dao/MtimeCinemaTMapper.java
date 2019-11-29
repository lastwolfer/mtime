package com.stylefeng.guns.cinema.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.cinema.persistence.model.MtimeCinemaT;
import com.stylefeng.guns.service.cinema.vo.CinemaGetFieldsVO;

/**
 * <p>
 * 影院信息表 Mapper 接口
 * </p>
 *
 * @author pandax
 * @since 2019-11-28
 */
public interface MtimeCinemaTMapper extends BaseMapper<MtimeCinemaT> {

    CinemaGetFieldsVO getFileds(Integer id);
}
