package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.common.persistence.model.MtimeCinemaT;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeCinemaTMapper;
import com.stylefeng.guns.service.cinema.CinemaService;
import com.stylefeng.guns.service.cinema.vo.CinemasDataVo;
import com.stylefeng.guns.service.cinema.vo.CinemasReqVo;
import com.stylefeng.guns.service.cinema.vo.RespVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import service.IMtimeCinemaTService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 影院信息表 服务实现类
 * </p>
 *
 * @author pandax
 * @since 2019-11-28
 */
@Component
@Service(interfaceClass = CinemaService.class)
public class CinemaServiceImpl implements CinemaService {

    @Autowired
    MtimeCinemaTMapper mtimeCinemaTMapper;

    /**
     * 获取影院信息
     * @param cinemasReqVo
     * @return
     */
    @Override
    public RespVo getCinemas(CinemasReqVo cinemasReqVo) {
        RespVo respVo = new RespVo();
        Wrapper<MtimeCinemaT> mtimeCinemaTWrapper = new EntityWrapper<>();
        if (cinemasReqVo.getBrandId() != 99) {
            mtimeCinemaTWrapper.eq("brand_id", cinemasReqVo.getBrandId());
        }
        if (cinemasReqVo.getHallType() != 99) {
            mtimeCinemaTWrapper.like("hall_type", "#" + cinemasReqVo.getHallType() + "#");
        }
        if (cinemasReqVo.getAreaId() != 99) {
            mtimeCinemaTWrapper.eq("area_id", cinemasReqVo.getBrandId());
        }
        Page<MtimeCinemaT> page = new Page<>(cinemasReqVo.getNowPage(), cinemasReqVo.getPageSize());
        List<MtimeCinemaT> mtimeCinemaTList = mtimeCinemaTMapper.selectPage(page, mtimeCinemaTWrapper);
        if (CollectionUtils.isEmpty(mtimeCinemaTList)){
            return new RespVo(1,"影院信息查询失败");
        }
        List<CinemasDataVo> cinemasDataVos = new ArrayList<>();
        for (MtimeCinemaT mtimeCinemaT : mtimeCinemaTList) {
            CinemasDataVo cinemasDataVo = new CinemasDataVo();
            BeanUtils.copyProperties(mtimeCinemaT, cinemasDataVo);
            cinemasDataVos.add(cinemasDataVo);
        }
        long totalPage = (long) Math.ceil(page.getTotal()/(double)page.getSize());
        respVo.setStatus(0);
        respVo.setNowPage(cinemasReqVo.getNowPage());
        respVo.setTotalPage(totalPage);
        respVo.setData(cinemasDataVos);
        return respVo;
    }

    /**
     * 获取场次详情信息
     * @param map
     * @return
     */
    @Override
    public RespVo getFieldInfo(Map<String, Object> map) {

        return null;
    }
}
