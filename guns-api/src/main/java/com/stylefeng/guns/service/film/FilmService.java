package com.stylefeng.guns.service.film;

import com.stylefeng.guns.service.film.vo.FilmReqVo3;
import com.stylefeng.guns.service.film.vo.FilmVo;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 17:35
 */

public interface FilmService {
    FilmVo getFilms(FilmReqVo3 filmReqVo);

    FilmVo getFilmDetail(int searchType, String searchParam);
}
