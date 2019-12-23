package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeCatDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeSourceDictTMapper;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.common.persistence.model.MtimeCatDictT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeSourceDictT;
import com.stylefeng.guns.rest.persistence.dao.*;
import com.stylefeng.guns.rest.persistence.model.*;
import com.stylefeng.guns.rest.persistence.model.MtimeFilmT;
import com.stylefeng.guns.service.film.FilmService;
import com.stylefeng.guns.service.film.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


import java.util.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    MtimeFilmActorTMapper mtimeFilmActorTMapper;


    @Override
    public FilmsVo getFilms(FilmReqVo3 filmReqVo) {
        EntityWrapper<MtimeFilmT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_status", filmReqVo.getShowType());
        if(filmReqVo.getCatId() != 99) {
            wrapper.like("film_cats", "#" + filmReqVo.getCatId() + "#");
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
        /*if(CollectionUtils.isEmpty(mtimeFilmTS)){
            return null;
        }*/
        List<FilmInfoVo> list = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (MtimeFilmT mtimeFilm : mtimeFilmTS) {
            FilmInfoVo filmInfoVo = new FilmInfoVo();
            filmInfoVo.setFilmId(mtimeFilm.getUuid().toString());
            filmInfoVo.setFilmType(mtimeFilm.getFilmStatus());
            filmInfoVo.setImgAddress(mtimeFilm.getImgAddress());
            filmInfoVo.setFilmName(mtimeFilm.getFilmName());
            filmInfoVo.setScore(mtimeFilm.getFilmScore());
            filmInfoVo.setFilmScore(mtimeFilm.getFilmScore());
            filmInfoVo.setExpectNum(Integer.parseInt(mtimeFilm.getFilmPresalenum().toString()));
            filmInfoVo.setBoxNum(mtimeFilm.getFilmBoxOffice());
            filmInfoVo.setFilmCats("");
            Date filmTime = mtimeFilm.getFilmTime();
            String time = df.format(filmTime);
            filmInfoVo.setShowTime(time);
            list.add(filmInfoVo);
        }
        FilmsVo filmVo = new FilmsVo();
        //先暂时写这个，到时候在改
        //filmVo.setImgPre("https://da4j-1300799324.cos.ap-shanghai.myqcloud.com/");
        filmVo.setNowPage(filmReqVo.getNowPage());
        int total = (int)page.getTotal();
        filmVo.setTotalPage(total/filmReqVo.getPageSize() + 1);
        filmVo.setData(list);

        return filmVo;
    }

    @Override
    public List<BannerVo> getBanners() {
        EntityWrapper<MtimeBannerT> wrapper = new EntityWrapper<>();
        wrapper.eq("is_valid",0);
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

    @Value("${meeting.film.preImg}")
    String imgPre;
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public Map<String, Object> getIndex() {
        Map<String,Object> map = (Map<String, Object>) redisTemplate.opsForValue().get("index");
        System.out.println("map="+map);
        if(map==null){
            map=new HashMap<>();
            //1获取banners
//        EntityWrapper<MtimeBannerT> wrapper = new EntityWrapper<>();
//        List<MtimeBannerT> mtimeBannerTS = bannerTMapper.selectList(wrapper);
            map.put("banners",getBanners());
            //2.获取boxRanking
            List<FilmTVo> boxRank = getBoxRank();
            map.put("boxRanking", boxRank);
            //3.获取expectRanking
            map.put("expectRanking",getExceptRank());
            //4.hotFiles
            map.put("hotFilms",getHotFilms());
            //5.
            map.put("top100",getTop());
            map.put("soonFilms",getSoonFilms());

            redisTemplate.opsForValue().set("index",map);
            redisTemplate.expire("index",1000 , TimeUnit.MILLISECONDS);
        }
        return map;
    }
    @Autowired
    MtimeCatDictTMapper catDictTMapper;
    @Override
    public List<CatVo> getCatDirt(Integer catId) {
        EntityWrapper<MtimeCatDictT> wrapper = new EntityWrapper<>();
        List<MtimeCatDictT> mtimeCatDictTS = catDictTMapper.selectList(wrapper);
        ArrayList<CatVo> catVos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(mtimeCatDictTS)){
            for (MtimeCatDictT mtimeCatDictT : mtimeCatDictTS) {
                CatVo catVo = new CatVo();
                catVo.setCatId(mtimeCatDictT.getUuid()+"");
                catVo.setCatName(mtimeCatDictT.getShowName());
                if(catId==99){
                    if("全部".equals(mtimeCatDictT.getShowName())){
                        catVo.setActive(true);
                    }
                }else{
                    if((""+catId).equals(mtimeCatDictT.getUuid())){
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
        Map<String,Object> map= (Map<String, Object>) redisTemplate.opsForValue().get("condition");
        System.out.println("condition="+map);
        if(map==null){
            List<CatVo> catDirt = getCatDirt(catId);
            List<SourceVo> sources = getSources(sourceId);
            List<YearVo> byYear = getByYear(yearId);
            map = new HashMap<>();
            map.put("catInfo",catDirt);
            map.put("sourceInfo",sources);
            map.put("yearInfo",byYear);
            redisTemplate.opsForValue().set("condition",map);
            redisTemplate.expire("index",1000 , TimeUnit.MILLISECONDS);
        }
        return map;
    }
    public FilmDetailVo getFilmDetail(int searchType, String searchParam) {
        FilmDetailVo filmDetailVo = new FilmDetailVo();
        MtimeFilmT mtimeFilm = null;
        if(searchType == 0) {
            mtimeFilm = mtimeFilmTMapper.selectById(searchParam);
            if(mtimeFilm == null) {
                return null;
            }
        } else {
            EntityWrapper<MtimeFilmT> wrapper = new EntityWrapper<>();
            wrapper.eq("film_name", searchParam);
            List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectList(wrapper);
            if(CollectionUtils.isEmpty(mtimeFilmTS)){
                return null;
            }
            mtimeFilm = mtimeFilmTS.get(0);
        }

        Integer id = mtimeFilm.getUuid();
        EntityWrapper<MtimeFilmInfoT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_id", id);

        List<MtimeFilmInfoT> mtimeFilmInfoList = mtimeFilmInfoTMapper.selectList(wrapper);
        /*if(CollectionUtils.isEmpty(mtimeFilmInfoList)) {
            throw new GunsException(BizExceptionEnum.SYS_ERROR);
        }*/
        MtimeFilmInfoT mtimeFilmInfo = mtimeFilmInfoList.get(0);
        filmDetailVo.setFilmId(id.toString());
        filmDetailVo.setFilmName(mtimeFilm.getFilmName());
        filmDetailVo.setFilmEnName(mtimeFilmInfo.getFilmEnName());
        filmDetailVo.setImgAddress(mtimeFilm.getImgAddress());
        filmDetailVo.setScore(mtimeFilmInfo.getFilmScore());
        filmDetailVo.setScoreNum(mtimeFilmInfo.getFilmScoreNum().toString());
        filmDetailVo.setTotalBox(mtimeFilm.getFilmBoxOffice().toString());

        EntityWrapper<MtimeHallFilmInfoT> hallFilmInfoTEntityWrapper = new EntityWrapper<>();
        hallFilmInfoTEntityWrapper.eq("film_id", id);
        List<MtimeHallFilmInfoT> mtimeHallFilmInfoTList = mtimeHallFilmInfoTMapper.selectList(hallFilmInfoTEntityWrapper);
        /*if(CollectionUtils.isEmpty(mtimeHallFilmInfoTList)) {
            throw new GunsException(BizExceptionEnum.SYS_ERROR);
        }*/
        MtimeHallFilmInfoT mtimeHallFilmInfo = mtimeHallFilmInfoTList.get(0);
        /*String[] catIds = mtimeFilm.getFilmCats().split("#");
        System.out.println(Arrays.toString(catIds));
        StringBuilder sb = new StringBuilder();
        for (String catId : catIds) {
            MtimeCatDictT catDict = catDictTMapper.selectById(catId);
            sb.append(catDict.getShowName()).append(",");
        }
        sb.delete(sb.length() - 1, sb.length());*/

        filmDetailVo.setInfo01(mtimeHallFilmInfo.getFilmCats());

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
        EntityWrapper<MtimeFilmActorT> mtimeFilmActorTEntityWrapper = new EntityWrapper<>();
        mtimeFilmActorTEntityWrapper.eq("film_id", mtimeFilm.getUuid());
        List<MtimeFilmActorT> roleVos = mtimeFilmActorTMapper.selectList(mtimeFilmActorTEntityWrapper);
        for (MtimeFilmActorT roleVo : roleVos) {
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
        infoRequestVO.setImgVO(imgVO);
        infoRequestVO.setFilmId(id.toString());
        return filmDetailVo;
    }

    @Override
    public String getFilmNameById(Integer uuid) {
        MtimeFilmT mtimeFilmT = mtimeFilmTMapper.selectById(uuid);
        return mtimeFilmT.getFilmName();
    }
}
