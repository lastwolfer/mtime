package com.stylefeng.guns.service.film;

import com.stylefeng.guns.service.film.vo.FilmReqVo3;
import com.stylefeng.guns.service.film.vo.FilmVo3;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 17:35
 */

public interface FilmService {
    FilmVo3 getFilms(FilmReqVo3 filmReqVo);
}
