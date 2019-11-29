package com.stylefeng.guns.service.film;

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
    FilmsVo getFilms(FilmReqVo3 filmReqVo);

    FilmDetailVo getFilmDetail(int searchType, String searchParam);
}
