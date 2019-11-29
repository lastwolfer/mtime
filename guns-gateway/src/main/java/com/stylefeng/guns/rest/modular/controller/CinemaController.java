package com.stylefeng.guns.rest.modular.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.service.cinema.ConditionService;
import com.stylefeng.guns.service.cinema.vo.CinemaCondition;
import com.stylefeng.guns.service.cinema.vo.RespVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("cinema")
public class CinemaController {

    @Reference(interfaceClass = ConditionService.class,check = false)
    ConditionService conditionService;

    @RequestMapping("getCondition")
    public RespVo getCondition(CinemaCondition cinemaCondition){
        RespVo respVo = conditionService.getCondition(cinemaCondition);
        return respVo;
    }
}
