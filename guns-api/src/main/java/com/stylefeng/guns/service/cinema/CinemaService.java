package com.stylefeng.guns.service.cinema;

import com.stylefeng.guns.service.cinema.vo.CinemasReqVo;
import com.stylefeng.guns.service.cinema.vo.RespVo;

import java.util.Map;

public interface CinemaService {

    RespVo getCinemas(CinemasReqVo cinemasReqVo);

    RespVo getFieldInfo(Integer fieldId,Integer cinemaId);
}
