package com.stylefeng.guns.cinema.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.service.cinema.CinemaService;
import com.stylefeng.guns.service.cinema.vo.CinemaGetFieldsVO;
import com.stylefeng.guns.service.cinema.vo.FilmFieldsVo;
import com.stylefeng.guns.service.cinema.vo.CFilmVo;
import com.stylefeng.guns.cinema.persistence.dao.MtimeFieldTMapper;
import com.stylefeng.guns.cinema.persistence.dao.MtimeHallFilmInfoTMapper;
import com.stylefeng.guns.service.cinema.beans.MtimeCinemaT;
import com.stylefeng.guns.cinema.persistence.dao.MtimeCinemaTMapper;
import com.stylefeng.guns.service.cinema.beans.MtimeFieldT;
import com.stylefeng.guns.service.cinema.beans.MtimeHallFilmInfoT;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <p>
 * 影院信息表 服务实现类
 * </p>
 *
 * @author pandax
 * @since 2019-11-28
 */
@Component
@Service
public class CinemaServiceImpl implements CinemaService {

    @Autowired
    MtimeCinemaTMapper mtimeCinemaTMapper;
    @Autowired
    MtimeFieldTMapper mtimeFieldTMapper;
    @Autowired
    MtimeHallFilmInfoTMapper mtimeHallFilmInfoTMapper;

    @Override
    public CinemaGetFieldsVO getFileds(Integer id) {
        MtimeCinemaT mtimeCinemaT = mtimeCinemaTMapper.selectById(id);
        CinemaGetFieldsVO cinemaGetFieldsVO = new CinemaGetFieldsVO();
        transCGFV(mtimeCinemaT,cinemaGetFieldsVO);

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


        cinemaGetFieldsVO.setFilmList(CFilmVos);

        return cinemaGetFieldsVO;
    }

    private void transCGFV(MtimeCinemaT mtimeCinemaT,CinemaGetFieldsVO cinemaGetFieldsVO){
        cinemaGetFieldsVO.setCinemaId(mtimeCinemaT.getUuid());
        cinemaGetFieldsVO.setCinemaAdress(mtimeCinemaT.getCinemaAddress());
        cinemaGetFieldsVO.setCinemaName(mtimeCinemaT.getCinemaName());
        cinemaGetFieldsVO.setCinemaPhone(mtimeCinemaT.getCinemaPhone());
        cinemaGetFieldsVO.setImgUrl(mtimeCinemaT.getImgAddress());
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
