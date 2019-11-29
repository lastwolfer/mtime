package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.rest.common.exception.BizExceptionEnum;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeAreaDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeBrandDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeHallDictTMapper;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.stylefeng.guns.service.cinema.ConditionService;
import com.stylefeng.guns.service.cinema.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service(interfaceClass = ConditionService.class)
public class ConditionServiceImpl implements ConditionService {
    @Autowired
    private MtimeAreaDictTMapper mtimeAreaDictTMapper;
    @Autowired
    private MtimeBrandDictTMapper mtimeBrandDictTMapper;
    @Autowired
    private MtimeHallDictTMapper mtimeHallDictTMapper;
    @Override
    public RespVo getCondition(CinemaCondition cinemaCondition) {
        Integer brandId = cinemaCondition.getBrandId();
        Integer hallType = cinemaCondition.getHallType();
        Integer areaId = cinemaCondition.getAreaId();
        EntityWrapper<MtimeAreaDictT> mtimeAreaDictTEntityWrapper = new EntityWrapper<>();
        EntityWrapper<MtimeBrandDictT> mtimeBrandDicTEntityWrapper = new EntityWrapper<>();
        EntityWrapper<MtimeHallDictT> mtimeHallDictTEntityWrapper = new EntityWrapper<>();

        List<MtimeAreaDictT> mtimeAreaDictTS = mtimeAreaDictTMapper.selectList(mtimeAreaDictTEntityWrapper);
        List<AreaVo> areaVoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mtimeAreaDictTS)) {
            for (MtimeAreaDictT mtimeAreaDictT : mtimeAreaDictTS) {
                AreaVo areaVo = new AreaVo();
                areaVo.setActive(false);
                areaVo.setAreaId(mtimeAreaDictT.getUuid());
                areaVo.setAreaName(mtimeAreaDictT.getShowName());
                if (areaId.equals(mtimeAreaDictT.getUuid())) {
                    areaVo.setActive(true);
                }
                areaVoList.add(areaVo);
            }
        }

        List<MtimeBrandDictT> mtimeBrandDictTS = mtimeBrandDictTMapper.selectList(mtimeBrandDicTEntityWrapper);
        List<BrandVo> brandVoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mtimeBrandDictTS)) {
            for (MtimeBrandDictT mtimeBrandDictT : mtimeBrandDictTS) {
                BrandVo brandVo = new BrandVo();
                brandVo.setActive(false);
                brandVo.setBrandId(mtimeBrandDictT.getUuid());
                brandVo.setBrandName(mtimeBrandDictT.getShowName());
                if (brandId.equals(mtimeBrandDictT.getUuid())) {
                    brandVo.setActive(true);
                }
                brandVoList.add(brandVo);
            }
        }

        List<MtimeHallDictT> mtimeHallDictTS = mtimeHallDictTMapper.selectList(mtimeHallDictTEntityWrapper);
        List<HallVo> hallVoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mtimeHallDictTS)) {
            for (MtimeHallDictT mtimeHallDictT : mtimeHallDictTS) {
                HallVo hallVo = new HallVo();
                hallVo.setActive(false);
                hallVo.setHalltypeId(mtimeHallDictT.getUuid());
                hallVo.setHalltypeName(mtimeHallDictT.getShowName());
                if (hallType.equals(mtimeHallDictT.getUuid())) {
                    hallVo.setActive(true);
                }
                hallVoList.add(hallVo);
            }
        }
        //组合成响应参数
        if (CollectionUtils.isEmpty(mtimeAreaDictTS) || CollectionUtils.isEmpty(mtimeBrandDictTS) || CollectionUtils.isEmpty(mtimeHallDictTS)) {
            throw new GunsException(BizExceptionEnum.AUTH_REQUEST_ERROR);
        }
        Map<String, Object> respoMap = new HashMap<>();
        respoMap.put("areaList", areaVoList);
        respoMap.put("brandList", brandVoList);
        respoMap.put("halltypeList", hallVoList);
        RespVo respVo = new RespVo();
        respVo.setStatus(0);
        respVo.setData(respoMap);
        return  respVo;
    }
}
