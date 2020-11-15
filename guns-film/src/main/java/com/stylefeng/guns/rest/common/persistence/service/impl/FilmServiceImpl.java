package com.stylefeng.guns.rest.common.persistence.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeBannerTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFilmTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeBannerT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmT;
import com.stylefeng.guns.service.cinema.vo.RespVo;
import com.stylefeng.guns.service.film.FilmService;
import com.stylefeng.guns.service.film.vo.FilmVo;
import com.stylefeng.guns.service.film.vo.MtimeBannerVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Component
@Service
public class FilmServiceImpl implements FilmService {
    @Autowired
    MtimeBannerTMapper mtimeBannerTMapper;
    @Autowired
    MtimeFilmTMapper mtimeFilmTMapper;

    @Override
    public RespVo getIndex() {
        RespVo respVo = new RespVo();
        HashMap<String, Object> map = new HashMap<>();

        //取出所有banner首页大图片信息
        EntityWrapper<MtimeBannerT> bannerTEntityWrapper = new EntityWrapper<>();
        List<MtimeBannerVo> bannerVoList = new ArrayList<>();
        List<MtimeBannerT> mtimeBannerTS = mtimeBannerTMapper.selectList(bannerTEntityWrapper);
        if(mtimeBannerTS != null ){
            for (MtimeBannerT mtimeBannerT : mtimeBannerTS) {
                MtimeBannerVo mtimeBannerVo = new MtimeBannerVo(mtimeBannerT.getUuid(),
                        mtimeBannerT.getBannerAddress(),
                        mtimeBannerT.getBannerUrl());
                bannerVoList.add(mtimeBannerVo);
            }

            map.put("banners",bannerVoList);
        }
        //取出所有的电影
        EntityWrapper<MtimeFilmT> filmTEntityWrapper = new EntityWrapper<>();
        List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectList(filmTEntityWrapper);
        //热映电影列表
        ArrayList<FilmVo> boxRanking = new ArrayList<>();
        //即将上映电影列表
        ArrayList<FilmVo> expectRanking = new ArrayList<>();
        for (MtimeFilmT mtimeFilmT : mtimeFilmTS) {
                FilmVo filmVo = new FilmVo();
                BeanUtils.copyProperties(mtimeFilmT,filmVo);
                filmVo.setFilmId(mtimeFilmT.getUuid());
                filmVo.setExpectNum(mtimeFilmT.getFilmPresalenum());
                filmVo.setBoxNum(mtimeFilmT.getFilmBoxOffice());
                filmVo.setShowTime(mtimeFilmT.getFilmTime());
                filmVo.setScore(mtimeFilmT.getFilmScore());
            //取出stateid=1正在热映的电影 根据当天票房高低降序desc
            if(mtimeFilmT.getFilmStatus() == 1) {
                boxRanking.add(filmVo);
            }
            //取出stateid=2即将上映的电影 根据预售票房高低降序desc
            if(mtimeFilmT.getFilmStatus() == 2){
                expectRanking.add(filmVo);
            }
            //hotFilms
            HashMap<String, Object> hotFilms = new HashMap<>();
            hotFilms.put("filmInfo",boxRanking);
            hotFilms.put("filmNum",boxRanking.size());
            map.put("hotFilms",hotFilms);
            //"soonFilms
            HashMap<String, Object> soonFilms = new HashMap<>();
            hotFilms.put("filmInfo",expectRanking);
            hotFilms.put("filmNum",expectRanking.size());
            map.put("soonFilms",soonFilms);
            //top100
            map.put("toop100",mtimeFilmTS);
        }
        String preUrl = "http://img.meetingshop.cn";
        map.put("imgPre",preUrl);
        respVo.setStatus(0);
        respVo.setData(map);
        respVo.setImgPre(preUrl);
        return respVo;
    }
}
