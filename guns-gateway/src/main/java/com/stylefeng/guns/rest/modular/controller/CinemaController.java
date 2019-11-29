package com.stylefeng.guns.rest.modular.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.service.cinema.CinemaService;
import com.stylefeng.guns.service.cinema.ConditionService;
import com.stylefeng.guns.service.cinema.vo.CinemaCondition;
import com.stylefeng.guns.service.cinema.vo.CinemaGetFieldsVO;
import com.stylefeng.guns.service.cinema.vo.Resp3Vo;
import com.stylefeng.guns.service.cinema.vo.RespVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("cinema")
public class CinemaController {

    @Reference(interfaceClass = ConditionService.class,check = false)
    ConditionService conditionService;

    @Reference(interfaceClass = CinemaService.class)
    CinemaService cinemaService;


    @RequestMapping("getCondition")
    public RespVo getCondition(CinemaCondition cinemaCondition){
        RespVo respVo = conditionService.getCondition(cinemaCondition);
        return respVo;
    }

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
