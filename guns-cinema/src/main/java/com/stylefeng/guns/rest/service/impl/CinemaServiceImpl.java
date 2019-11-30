package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFieldTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeHallDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeHallFilmInfoTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeCinemaT;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeCinemaTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFieldT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeHallDictT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeHallFilmInfoT;
import com.stylefeng.guns.service.cinema.CinemaService;
import com.stylefeng.guns.service.cinema.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    MtimeFieldTMapper mtimeFieldTMapper;

    @Autowired
    MtimeHallFilmInfoTMapper mtimeHallFilmInfoTMapper;

    @Autowired
    MtimeHallDictTMapper mtimeHallDictTMapper;


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
        respVo.setTotalPage((int) totalPage);
        respVo.setData(cinemasDataVos);
        return respVo;
    }

    /**
     * 获取场次详情信息
     * @param
     * @return
     */
    @Override
    public RespVo getFieldInfo(Integer fieldId,Integer cinemaId) {
        if (fieldId==null && cinemaId==null){
            return new RespVo(1,"参数异常");
        }
        CinemaFilmInfoVo cinemaFilmInfoVo =  null;
        CinemaInfoVo cinemaInfoVo = null;
        HallInfoVo hallInfoVo = null;

        try {
            //先获取filmID
            MtimeFieldT mtimeFieldT = mtimeFieldTMapper.selectById(fieldId);
            Integer filmId = mtimeFieldT.getFilmId();
            //查询film场次信息
            EntityWrapper<MtimeHallFilmInfoT> mtimeHallFilmInfoTEntityWrapper = new EntityWrapper<>();
            mtimeHallFilmInfoTEntityWrapper.eq("film_id", filmId);
            List<MtimeHallFilmInfoT> mtimeHallFilmInfoTS = mtimeHallFilmInfoTMapper.selectList(mtimeHallFilmInfoTEntityWrapper);
            MtimeHallFilmInfoT mtimeHallFilmInfoT = mtimeHallFilmInfoTS.get(0);

            cinemaFilmInfoVo = new CinemaFilmInfoVo();
            BeanUtils.copyProperties(mtimeHallFilmInfoT, cinemaFilmInfoVo);
            cinemaFilmInfoVo.setFilmType(mtimeHallFilmInfoT.getFilmLength());
            cinemaFilmInfoVo.setFilmId(String.valueOf(filmId));

            cinemaInfoVo = new CinemaInfoVo();
            MtimeCinemaT mtimeCinemaT = mtimeCinemaTMapper.selectById(cinemaId);
            BeanUtils.copyProperties(mtimeCinemaT,cinemaInfoVo);
            cinemaInfoVo.setCinemaId(cinemaId);
            cinemaInfoVo.setImgUrl(mtimeCinemaT.getImgAddress());

            hallInfoVo = new HallInfoVo();
            hallInfoVo.setHallFieldId(String.valueOf(mtimeFieldT.getHallId()));
            hallInfoVo.setHallName(mtimeFieldT.getHallName());
            hallInfoVo.setPrice(mtimeFieldT.getPrice());
            MtimeHallDictT mtimeHallDictT = mtimeHallDictTMapper.selectById(mtimeFieldT.getHallId());
            hallInfoVo.setSeatFile(mtimeHallDictT.getSeatAddress());
            hallInfoVo.setSoldSeats("1,2,3,5,12");
        } catch (BeansException e) {
//           //e.printStackTrace();
            return new RespVo(1,"查询失败");
        }

        FieldInfoRespDataVo fideInfoRespDataVo = new FieldInfoRespDataVo();
        fideInfoRespDataVo.setFilmInfo(cinemaFilmInfoVo);
        fideInfoRespDataVo.setCinemaInfo(cinemaInfoVo);
        fideInfoRespDataVo.setHallInfo(hallInfoVo);
        RespVo respVo = new RespVo();
        respVo.setStatus(0);
        respVo.setData(fideInfoRespDataVo);
        respVo.setImgPre("http://img.meetingshop.cn/");
        return respVo;
    }

    @Override
    public CinemaGetFieldsVo getFileds(Integer id) {
        CinemaGetFieldsVo cinemaGetFieldsVo = new CinemaGetFieldsVo();
        MtimeCinemaT mtimeCinemaT = mtimeCinemaTMapper.selectById(id);
        CinemaInfoVo cinemaInfoVo = new CinemaInfoVo();
        transCGFV(mtimeCinemaT,cinemaInfoVo);

        EntityWrapper<MtimeFieldT> mtimeFieldTEntityWrapper = new EntityWrapper<>();
        mtimeFieldTEntityWrapper.eq("cinema_id",id);
        List<MtimeFieldT> mtimeFieldTS = mtimeFieldTMapper.selectList(mtimeFieldTEntityWrapper);
        List<FilmFieldsVo> filmFieldsVos = new ArrayList<>();
        HashMap<Integer, List<FilmFieldsVo>> map = new HashMap<>();
        Integer filmId;
        for (int i = 0; i < mtimeFieldTS.size(); i++) {
            FilmFieldsVo filmFieldsVo = new FilmFieldsVo();
            transFFV(mtimeFieldTS.get(i),filmFieldsVo);
            filmFieldsVos.add(filmFieldsVo);
            filmId = mtimeFieldTS.get(i).getFilmId();
            if (map.containsKey(filmId)){
                List<FilmFieldsVo> fieldsVos = map.get(filmId);
                fieldsVos.add(filmFieldsVos.get(i));
            }else {
                List<FilmFieldsVo> fieldsVos = new ArrayList<>();
                fieldsVos.add(filmFieldsVos.get(i));
                map.put(filmId,fieldsVos);
            }
        }

        List<CFilmVo> CFilmVos = new ArrayList<>();
        for (Map.Entry<Integer, List<FilmFieldsVo>> entry : map.entrySet()) {
            MtimeHallFilmInfoT mtimeHallFilmInfoT = mtimeHallFilmInfoTMapper.selectById(entry.getKey());
            CFilmVo CFilmVo = new CFilmVo();
            transFV(mtimeHallFilmInfoT, CFilmVo);
            List<FilmFieldsVo> value = entry.getValue();
            for (FilmFieldsVo filmFieldsVo : value) {
                filmFieldsVo.setLanguage(mtimeHallFilmInfoT.getFilmLanguage());
            }
            CFilmVo.setFilmFields(value);
            CFilmVos.add(CFilmVo);
        }

        cinemaGetFieldsVo.setCinemaInfo(cinemaInfoVo);
        cinemaGetFieldsVo.setFilmList(CFilmVos);
        return cinemaGetFieldsVo;
    }

    private void transCGFV(MtimeCinemaT mtimeCinemaT,CinemaInfoVo cinemaInfoVo){
        cinemaInfoVo.setCinemaId(mtimeCinemaT.getUuid());
        cinemaInfoVo.setCinemaAddress(mtimeCinemaT.getCinemaAddress());
        cinemaInfoVo.setCinemaName(mtimeCinemaT.getCinemaName());
        cinemaInfoVo.setCinemaPhone(mtimeCinemaT.getCinemaPhone());
        cinemaInfoVo.setImgUrl(mtimeCinemaT.getImgAddress());
    }
    private void transFFV(MtimeFieldT mtimeFieldT,FilmFieldsVo f){
        f.setFieldId(mtimeFieldT.getUuid());
        f.setHallName(mtimeFieldT.getHallName());
        f.setPrice(mtimeFieldT.getPrice());
        f.setBeginTime(mtimeFieldT.getBeginTime());
        f.setEndTime(mtimeFieldT.getEndTime());
    }
    private void transFV(MtimeHallFilmInfoT mtimeHallFilmInfoT, CFilmVo CFilmVo){
        CFilmVo.setActors(mtimeHallFilmInfoT.getActors());
        CFilmVo.setFilmCats(mtimeHallFilmInfoT.getFilmCats());
        CFilmVo.setFilmId(mtimeHallFilmInfoT.getFilmId());
        CFilmVo.setFilmLength(mtimeHallFilmInfoT.getFilmLength());
        CFilmVo.setFilmType(mtimeHallFilmInfoT.getFilmLanguage());
        CFilmVo.setImgAddress(mtimeHallFilmInfoT.getImgAddress());
    }
}
