package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.persistence.dao.*;
import com.stylefeng.guns.rest.persistence.model.*;
import com.stylefeng.guns.service.film.FilmService;
import com.stylefeng.guns.service.film.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    MtimeFilmTMapper mtimeFilmTMapper;

    @Autowired
    MtimeFilmInfoTMapper mtimeFilmInfoTMapper;

    @Autowired
    MtimeHallFilmInfoTMapper mtimeHallFilmInfoTMapper;

    @Autowired
    MtimeSourceDictTMapper mtimeSourceDictTMapper;

    @Autowired
    MtimeActorTMapper mtimeActorTMapper;


    @Override
    public FilmVo getFilms(FilmReqVo3 filmReqVo) {
        EntityWrapper<MtimeFilmT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_status", filmReqVo.getShowType());
        if(filmReqVo.getCatId() != 99) {
            wrapper.eq("film_cats", "#" + filmReqVo.getCatId() + "#");
        }
        if(filmReqVo.getSourceId() != 99) {
            wrapper.eq("film_source", filmReqVo.getSortId());
        }
        if(filmReqVo.getYearId() != 99) {
            wrapper.eq("film_date", filmReqVo.getYearId());
        }
        Integer sortId = filmReqVo.getSortId();
        if(sortId == 1) {
            wrapper.orderBy("film_box_office", false);
        } else if (sortId == 2){
            wrapper.orderBy("film_date", false);
        } else if (sortId == 3){
            wrapper.orderBy("film_score", false);
        }
        Page<MtimeFilmT> page = new Page<>(filmReqVo.getNowPage(), filmReqVo.getPageSize());
        List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectPage(page, wrapper);
        if(CollectionUtils.isEmpty(mtimeFilmTS)){
            return FilmVo.fail(1, "查询失败，无影片可加载");
        }
        List<FilmInfoVo> list = new ArrayList<>();
        for (MtimeFilmT mtimeFilm : mtimeFilmTS) {
            FilmInfoVo filmInfoVo = new FilmInfoVo();
            filmInfoVo.setFilmId(mtimeFilm.getUuid());
            filmInfoVo.setFilmType(mtimeFilm.getFilmStatus());
            filmInfoVo.setImgAddress(mtimeFilm.getImgAddress());
            filmInfoVo.setFilmName(mtimeFilm.getFilmName());
            filmInfoVo.setFilmScore(mtimeFilm.getFilmScore());
            list.add(filmInfoVo);
        }
        FilmsVo filmVo = new FilmsVo();
        //先暂时写这个，带时候在改
        filmVo.setImgPre("https://da4j-1300799324.cos.ap-shanghai.myqcloud.com/");
        filmVo.setNowPage(filmReqVo.getNowPage());
        int total = (int)page.getTotal();
        filmVo.setTotalPage(total);
        filmVo.setData(list);
        filmVo.setStatus(0);

        return filmVo;
    }

    @Override
    public FilmVo getFilmDetail(int searchType, String searchParam) {
        FilmDetailVo filmDetailVo = new FilmDetailVo();
        MtimeFilmT mtimeFilm = null;
        if(searchType == 0) {
            mtimeFilm = mtimeFilmTMapper.selectById(searchParam);
            if(mtimeFilm == null) {
                return FilmVo.fail(1, "查询失败，无影片可加载");
            }
        } else {
            EntityWrapper<MtimeFilmT> wrapper = new EntityWrapper<>();
            wrapper.eq("film_name", searchParam);
            List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectList(wrapper);
            if(CollectionUtils.isEmpty(mtimeFilmTS)){
                return FilmVo.fail(1, "查询失败，无影片可加载");
            }
            mtimeFilm = mtimeFilmTS.get(0);
        }

        Integer id = mtimeFilm.getUuid();
        EntityWrapper<MtimeFilmInfoT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_id", id);

        List<MtimeFilmInfoT> mtimeFilmInfoList = mtimeFilmInfoTMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(mtimeFilmInfoList)) {
            return FilmVo.fail(999, "系统出现异常，请联系管理员");
        }
        MtimeFilmInfoT mtimeFilmInfo = mtimeFilmInfoList.get(0);
        filmDetailVo.setFilmId(id.toString());
        filmDetailVo.setFilmName(mtimeFilm.getFilmName());
        filmDetailVo.setFilmEnName(mtimeFilmInfo.getFilmEnName());
        filmDetailVo.setFilmAddress(mtimeFilm.getImgAddress());
        filmDetailVo.setScore(mtimeFilmInfo.getFilmScore());
        filmDetailVo.setScoreNum(mtimeFilmInfo.getFilmScoreNum().toString());
        filmDetailVo.setTotalBox(mtimeFilm.getFilmBoxOffice().toString());

        EntityWrapper<MtimeHallFilmInfoT> hallFilmInfoTEntityWrapper = new EntityWrapper<>();
        hallFilmInfoTEntityWrapper.eq("film_id", id);
        List<MtimeHallFilmInfoT> mtimeHallFilmInfoTList = mtimeHallFilmInfoTMapper.selectList(hallFilmInfoTEntityWrapper);
        if(CollectionUtils.isEmpty(mtimeHallFilmInfoTList)) {
            return FilmVo.fail(999, "系统出现异常，请联系管理员");
        }
        MtimeHallFilmInfoT mtimeHallFilmInfo = mtimeHallFilmInfoTList.get(0);
        filmDetailVo.setInfo01(mtimeHallFilmInfo.getActors());

        MtimeSourceDictT mtimeSourceDict = mtimeSourceDictTMapper.selectById(mtimeFilm.getFilmSource());
        filmDetailVo.setInfo02(mtimeSourceDict.getShowName() + "/" + mtimeHallFilmInfo.getFilmLength() + "分钟");
        Date filmTime = mtimeFilm.getFilmTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String time = df.format(filmTime);
        filmDetailVo.setInfo03(time + mtimeSourceDict.getShowName() + "上映");
        InfoRequestVO infoRequestVO = new InfoRequestVO();
        infoRequestVO.setBiography(mtimeFilmInfo.getBiography());
        ActorsInfo actors = new ActorsInfo();
        MtimeActorT directorFromDB = mtimeActorTMapper.selectById(mtimeFilmInfo.getDirectorId());
        Director director = new Director();
        director.setImgAddress(directorFromDB.getActorImg());
        director.setDirectorName(directorFromDB.getActorName());

        actors.setDirector(director);
        ArrayList<Actor> actorArrayList = new ArrayList<>();
        List<RoleVo> list = mtimeActorTMapper.selectByFilmId(mtimeFilm.getUuid());
        for (RoleVo roleVo : list) {
            MtimeActorT mtimeActorT = mtimeActorTMapper.selectById(roleVo.getActorId());
            Actor actor = new Actor();
            actor.setImgAddress(mtimeActorT.getActorImg());
            actor.setDirectorName(mtimeActorT.getActorName());
            actor.setRoleName(roleVo.getRoleName());
            actorArrayList.add(actor);
        }
        actors.setActors(actorArrayList);
        infoRequestVO.setActors(actors);

        filmDetailVo.setInfo04(infoRequestVO);

        String[] imgArr = mtimeFilmInfo.getFilmImgs().split(",");
        FilmImgVo imgVO = new FilmImgVo();
        imgVO.setMainImg(imgArr.length > 0 ? imgArr[0] : "");
        imgVO.setImg01(imgArr.length > 1 ? imgArr[1] : "");
        imgVO.setImg02(imgArr.length > 2 ? imgArr[2] : "");
        imgVO.setImg03(imgArr.length > 3 ? imgArr[3] : "");
        imgVO.setImg04(imgArr.length > 4 ? imgArr[4] : "");

        filmDetailVo.setImgVO(imgVO);
        filmDetailVo.setStatus(0);
        filmDetailVo.setImgPre("https://da4j-1300799324.cos.ap-shanghai.myqcloud.com/");


        return filmDetailVo;
    }


}
