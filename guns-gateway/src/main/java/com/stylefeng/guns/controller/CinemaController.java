package com.stylefeng.guns.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.service.cinema.CinemaService;
import com.stylefeng.guns.service.cinema.vo.CinemaGetFieldsVO;
import com.stylefeng.guns.service.cinema.vo.Resp3Vo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CinemaController {

    @Reference(interfaceClass = CinemaService.class)
    CinemaService cinemaService;

    @RequestMapping("cinema/getFields")
    public Resp3Vo getFields(Integer cinemaId){
        Resp3Vo resp3Vo = new Resp3Vo();
        CinemaGetFieldsVO fileds = cinemaService.getFileds(cinemaId);
        resp3Vo.setData(fileds);
        resp3Vo.setImgPre("http://img.meetingshop.cn/");
        resp3Vo.setStatus(0);
        return resp3Vo;
    }
}
