package com.stylefeng.guns.service.film;


import com.stylefeng.guns.service.film.vo.*;

import java.util.List;
import java.util.Map;
import com.stylefeng.guns.service.film.vo.FilmDetailVo;
import com.stylefeng.guns.service.film.vo.FilmReqVo3;
import com.stylefeng.guns.service.film.vo.FilmsVo;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 17:35
 */

public interface FilmService {



    List<BannerVo> getBanners();

    List<FilmTVo> getBoxRank();

    List<FilmTVo> getExceptRank();

    Map<String,Object> getHotFilms();

    Map<String,Object> getSoonFilms();

    List<FilmTVo> getTop();

    Map<String,Object> getIndex();

    List<CatVo> getCatDirt(Integer areaId);

    List<SourceVo> getSources(Integer sourceId);

    List<YearVo> getByYear(Integer yearId);

    Map<String,Object> getCondition(Integer catId,Integer sourceId,Integer yearId);

    FilmsVo getFilms(FilmReqVo3 filmReqVo);

    FilmDetailVo getFilmDetail(int searchType, String searchParam);
}
