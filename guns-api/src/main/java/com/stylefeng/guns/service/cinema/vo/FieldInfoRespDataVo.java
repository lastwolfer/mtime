package com.stylefeng.guns.service.cinema.vo;

import com.stylefeng.guns.service.film.vo.FilmInfoVo;
import lombok.Data;

import java.io.Serializable;

/**
 * 影院详情信息的返回data
 */
@Data
public class FieldInfoRespDataVo implements Serializable {

    private static final long serialVersionUID = -1420167818656957906L;

    private CinemaFilmInfoVo filmInfo;

    private CinemaInfoVo cinemaInfo;

    private HallInfoVo hallInfo;
}
