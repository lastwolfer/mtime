package com.stylefeng.guns.cinema.service;

import com.stylefeng.guns.cinema.controller.CinemaGetFieldsVO;
import com.stylefeng.guns.cinema.persistence.model.MtimeCinemaT;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 影院信息表 服务类
 * </p>
 *
 * @author pandax
 * @since 2019-11-28
 */
public interface IMtimeCinemaTService extends IService<MtimeCinemaT> {
    CinemaGetFieldsVO getFileds(Integer id);
}
