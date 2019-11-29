package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.stylefeng.guns.service.film.FilmService;
import com.stylefeng.guns.service.film.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 17:45
 */
@Component
@Service(interfaceClass = FilmService.class)
public class FilmServiceImpl implements FilmService {

    @Autowired
    MtimeBannerTMapper bannerTMapper;
    @Override
    public FilmVo3 getFilms(FilmReqVo3 filmReqVo) {
        return null;
    }

    @Override
    public List<BannerVo> getBanners() {
        EntityWrapper<MtimeBannerT> wrapper = new EntityWrapper<>();
        wrapper.eq("is_valid",1);
        List<MtimeBannerT> mtimeBannerTS = bannerTMapper.selectList(wrapper);
        ArrayList<BannerVo> bannerVos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(mtimeBannerTS)){
            for (MtimeBannerT mtimeBannerT : mtimeBannerTS) {
                BannerVo bannerVo = new BannerVo();
                bannerVo.setBannerId(mtimeBannerT.getUuid());
                bannerVo.setBannerAddress(mtimeBannerT.getBannerAddress());
                bannerVo.setBannerUrl(mtimeBannerT.getBannerUrl());
                bannerVos.add(bannerVo);
            }
        }
        return bannerVos;
    }

    @Autowired
    MtimeFilmTMapper filmTMapper;
    @Override
    public List<FilmTVo> getBoxRank() {
        EntityWrapper<MtimeFilmT> wrapper = new EntityWrapper<>();
        wrapper.orderBy("film_box_office",false);
//        wrapper.eq("film_type",0);
        List<MtimeFilmT> mtimeFilmTS = filmTMapper.selectList(wrapper);
        ArrayList<FilmTVo> filmTVos=MtimeFilmTList2FilmTVoList(mtimeFilmTS);
        return filmTVos;
    }

    @Override
    public List<FilmTVo> getExceptRank() {
        EntityWrapper<MtimeFilmT> wrapper = new EntityWrapper<>();
        wrapper.orderBy("film_preSaleNum",false);
//        wrapper.eq("film_type",1);
        List<MtimeFilmT> mtimeFilmTS = filmTMapper.selectList(wrapper);
        ArrayList<FilmTVo> filmTVos=MtimeFilmTList2FilmTVoList(mtimeFilmTS);
        return filmTVos;
    }

    @Override
    public Map<String, Object> getHotFilms() {
        EntityWrapper<MtimeFilmT> wrapper = new EntityWrapper<>();
        wrapper.orderBy("film_box_office",false);
        List<MtimeFilmT> mtimeFilmTS = filmTMapper.selectList(wrapper);
        ArrayList<FilmTVo> filmTVos=MtimeFilmTList2FilmTVoList(mtimeFilmTS);
        HashMap<String, Object> map = new HashMap<>();
        map.put("filmInfo",filmTVos);
        map.put("filmNum",filmTVos.size());
        return map;
    }

    @Override
    public Map<String, Object> getSoonFilms() {
        EntityWrapper<MtimeFilmT> wrapper = new EntityWrapper<>();
        wrapper.orderBy("film_preSaleNum",false);
        wrapper.eq("film_status",2);
        List<MtimeFilmT> mtimeFilmTS = filmTMapper.selectList(wrapper);
        ArrayList<FilmTVo> filmTVos=MtimeFilmTList2FilmTVoList(mtimeFilmTS);
        HashMap<String, Object> map = new HashMap<>();
        map.put("filmInfo",filmTVos);
        map.put("filmNum",filmTVos.size());
        return map;
    }

    @Override
    public List<FilmTVo> getTop() {
        EntityWrapper<MtimeFilmT> wrapper = new EntityWrapper<>();
        wrapper.orderBy("film_box_office",false);
//        wrapper.eq("film_type",0);
        List<MtimeFilmT> mtimeFilmTS = filmTMapper.selectList(wrapper);
        ArrayList<FilmTVo> filmTVos=MtimeFilmTList2FilmTVoList(mtimeFilmTS);
        return filmTVos;
    }

    private ArrayList<FilmTVo> MtimeFilmTList2FilmTVoList(List<MtimeFilmT> mtimeFilmTS) {
        ArrayList<FilmTVo> filmTVos = new ArrayList<>();
        if(mtimeFilmTS!=null){
            for (MtimeFilmT mtimeFilmT : mtimeFilmTS) {
                FilmTVo filmTVo = new FilmTVo();
                filmTVo.setFilmCats("");
                filmTVo.setScore(mtimeFilmT.getFilmScore());
                filmTVo.setExpectNum(mtimeFilmT.getFilmPresalenum());
                filmTVo.setBoxNum(mtimeFilmT.getFilmBoxOffice());
                filmTVo.setFilmId(mtimeFilmT.getUuid());
                filmTVo.setFilmLength("");
                filmTVo.setFilmType(mtimeFilmT.getFilmType());
                filmTVo.setShowTime(mtimeFilmT.getFilmTime());
                filmTVo.setFilmName(mtimeFilmT.getFilmName());
                filmTVo.setImgAddress(mtimeFilmT.getImgAddress());
                filmTVo.setFilmScore(mtimeFilmT.getFilmScore());
                filmTVos.add(filmTVo);
            }
        }
        return filmTVos;
    }

    @Override
    public Map<String, Object> getIndex() {
        HashMap<String, Object> map = new HashMap<>();
        //1获取banners
        EntityWrapper<MtimeBannerT> wrapper = new EntityWrapper<>();
        List<MtimeBannerT> mtimeBannerTS = bannerTMapper.selectList(wrapper);

        map.put("banners",mtimeBannerTS);
        //2.获取boxRanking
        List<FilmTVo> boxRank = getBoxRank();
        map.put("boxRanking", boxRank);
        //3.获取expectRanking
        map.put("exceptRanking",getExceptRank());
        //4.hotFiles
        map.put("hotFilms",getHotFilms());
        //5.
        map.put("top100",getTop());
        map.put("soonFilms",getSoonFilms());
        return map;
    }


    @Autowired
    MtimeCatDictTMapper catDictTMapper;
    @Override
    public List<CatVo> getCatDirt(Integer areaId) {
        EntityWrapper<MtimeCatDictT> wrapper = new EntityWrapper<>();
        List<MtimeCatDictT> mtimeCatDictTS = catDictTMapper.selectList(wrapper);
        ArrayList<CatVo> catVos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(mtimeCatDictTS)){
            for (MtimeCatDictT mtimeCatDictT : mtimeCatDictTS) {
                CatVo catVo = new CatVo();
                catVo.setAreaId(mtimeCatDictT.getUuid());
                catVo.setAreaName(mtimeCatDictT.getShowName());
                if(areaId==99){
                    if("全部".equals(mtimeCatDictT.getShowName())){
                        catVo.setActive(true);
                    }
                }else{
                    if((""+areaId).equals(mtimeCatDictT.getUuid())){
                        catVo.setActive(true);
                    }
                }
                catVos.add(catVo);
            }
        }
        return catVos;
    }

    @Autowired
    MtimeSourceDictTMapper sourceDictTMapper;
    @Override
    public List<SourceVo> getSources(Integer sourceId) {
        EntityWrapper<MtimeSourceDictT> wrapper = new EntityWrapper<>();
        List<MtimeSourceDictT> mtimeSourceDictTS = sourceDictTMapper.selectList(wrapper);
        ArrayList<SourceVo> sourceVos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(mtimeSourceDictTS)){
            for (MtimeSourceDictT mtimeCatDictT : mtimeSourceDictTS) {
                SourceVo sourceVo = new SourceVo();
                sourceVo.setSourceId(""+mtimeCatDictT.getUuid());
                sourceVo.setSourceName(mtimeCatDictT.getShowName());
                if(sourceId==99){
                    if("全部".equals(mtimeCatDictT.getShowName())){
                        sourceVo.setActive(true);
                    }
                }else{
                    if((""+sourceId).equals(mtimeCatDictT.getUuid())){
                        sourceVo.setActive(true);
                    }
                }
                sourceVos.add(sourceVo);
            }
        }
        return sourceVos;
    }

    @Autowired
    MtimeYearDictTMapper yearDictTMapper;
    @Override
    public List<YearVo> getByYear(Integer yearId) {
        EntityWrapper<MtimeYearDictT> wrapper = new EntityWrapper<>();
        List<MtimeYearDictT> mtimeYearDictTS = yearDictTMapper.selectList(wrapper);
        ArrayList<YearVo> yearVos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(mtimeYearDictTS)){
            for (MtimeYearDictT mtimeYearDictT : mtimeYearDictTS) {
                YearVo yearVo = new YearVo();
                yearVo.setYearName(mtimeYearDictT.getShowName());
                yearVo.setYearId(mtimeYearDictT.getUuid()+"");
                if(yearId==99){
                    if("全部".equals(mtimeYearDictT.getShowName())){
                        yearVo.setActive(true);
                    }
                }else{
                    if((""+yearId).equals(mtimeYearDictT.getUuid())){
                        yearVo.setActive(true);
                    }
                }
                yearVos.add(yearVo);
            }
        }
        return yearVos;
    }

    @Override
    public Map<String, Object> getCondition(Integer catId, Integer sourceId, Integer yearId) {
        List<CatVo> catDirt = getCatDirt(catId);
        List<SourceVo> sources = getSources(sourceId);
        List<YearVo> byYear = getByYear(yearId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("catInfo",catDirt);
        map.put("sourceInfo",sources);
        map.put("yearInfo",byYear);
        return map;
    }
}
