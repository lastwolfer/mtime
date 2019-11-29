package com.stylefeng.guns.rest.modular.film.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.service.film.FilmService;
import com.stylefeng.guns.service.film.vo.FilmDetailVo;
import com.stylefeng.guns.service.film.vo.FilmReqVo3;
import com.stylefeng.guns.service.film.vo.FilmsVo;
import com.stylefeng.guns.service.film.vo.response.BaseVoDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/29
 * @time 9:20
 */

@RestController
public class FilmController {

    @Reference(interfaceClass = FilmService.class, check = false)
    FilmService filmService;

    @Value("${meeting.film.preImg}")
    private String preImg;

    @RequestMapping("/film/getFilms")
    public BaseVoDetail queryFilms(FilmReqVo3 filmReqVo){
        BaseVoDetail baseVoDetail = new BaseVoDetail();
        FilmsVo films = filmService.getFilms(filmReqVo);
        if(films == null) {
            baseVoDetail.setStatus(1);
            baseVoDetail.setMsg("查询失败，无影片可加载");
            return baseVoDetail;
        }
        baseVoDetail.setImgPre(preImg);
        baseVoDetail.setNowPage(films.getNowPage());
        baseVoDetail.setTotalPage(films.getTotalPage());
        baseVoDetail.setDate(films.getData());
        baseVoDetail.setStatus(0);
        return baseVoDetail;
    }


    @RequestMapping("/film/films/{searchParam}")
    public BaseVoDetail queryFilmInfo(Integer searchType,
                                      @PathVariable("searchParam") String searchParam){
        FilmDetailVo filmDetail = filmService.getFilmDetail(searchType, searchParam);
        BaseVoDetail baseVoDetail = new BaseVoDetail();
        if (filmDetail == null) {
            baseVoDetail.setStatus(1);
            baseVoDetail.setMsg("查询失败，无影片可加载");
            return baseVoDetail;
        }
        baseVoDetail.setImgPre(preImg);
        baseVoDetail.setDate(filmDetail);
        baseVoDetail.setStatus(0);
        return baseVoDetail;
    }
}
