package com.stylefeng.guns.cinema.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.cinema.controller.CinemaGetFieldsVO;
import com.stylefeng.guns.cinema.controller.FilmFieldsVo;
import com.stylefeng.guns.cinema.controller.FilmVo;
import com.stylefeng.guns.cinema.persistence.dao.MtimeFieldTMapper;
import com.stylefeng.guns.cinema.persistence.dao.MtimeHallFilmInfoTMapper;
import com.stylefeng.guns.cinema.persistence.model.MtimeCinemaT;
import com.stylefeng.guns.cinema.persistence.dao.MtimeCinemaTMapper;
import com.stylefeng.guns.cinema.persistence.model.MtimeFieldT;
import com.stylefeng.guns.cinema.persistence.model.MtimeHallFilmInfoT;
import com.stylefeng.guns.cinema.service.IMtimeCinemaTService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 影院信息表 服务实现类
 * </p>
 *
 * @author pandax
 * @since 2019-11-28
 */
@Service
public class MtimeCinemaTServiceImpl extends ServiceImpl<MtimeCinemaTMapper, MtimeCinemaT> implements IMtimeCinemaTService {

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

        List<FilmVo> filmVos = new ArrayList<>();
        for (Map.Entry<Integer, List<FilmFieldsVo>> entry : map.entrySet()) {
            MtimeHallFilmInfoT mtimeHallFilmInfoT = mtimeHallFilmInfoTMapper.selectById(entry.getKey());
            FilmVo filmVo = new FilmVo();
            transFV(mtimeHallFilmInfoT,filmVo);
            List<FilmFieldsVo> value = entry.getValue();
            for (FilmFieldsVo filmFieldsVo : value) {
                filmFieldsVo.setLanguage(mtimeHallFilmInfoT.getFilmLanguage());
            }
            filmVo.setFilmFields(value);
            filmVos.add(filmVo);
        }


        cinemaGetFieldsVO.setFilmList(filmVos);

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
    private void transFV(MtimeHallFilmInfoT mtimeHallFilmInfoT,FilmVo filmVo){
        filmVo.setActors(mtimeHallFilmInfoT.getActors());
        filmVo.setFilmCats(mtimeHallFilmInfoT.getFilmCats());
        filmVo.setFilmId(mtimeHallFilmInfoT.getFilmId());
        filmVo.setFilmLength(mtimeHallFilmInfoT.getFilmLength());
        filmVo.setFilmType(mtimeHallFilmInfoT.getFilmLanguage());
        filmVo.setImgAddress(mtimeHallFilmInfoT.getImgAddress());
    }
}
