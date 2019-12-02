package com.stylefeng.guns.service.cinema;


import com.stylefeng.guns.service.cinema.vo.*;

public interface CinemaService {

    RespVo getCinemas(CinemasReqVo cinemasReqVo);

    RespVo getFieldInfo(Integer fieldId,Integer cinemaId);

    CinemaGetFieldsVo getFileds(Integer id);

    Boolean isTrueSeats(Integer fieldId, String seatId);

    FieldInfoForOrderVo getOrderField(Integer uuid);

    CinemaInfoVo getCinemaById(Integer cinemaId);

}
