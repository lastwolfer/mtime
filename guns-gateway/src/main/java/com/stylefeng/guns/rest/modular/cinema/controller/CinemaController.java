package com.stylefeng.guns.rest.modular.cinema.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.rest.common.exception.BizExceptionEnum;
import com.stylefeng.guns.service.cinema.CinemaService;
import com.stylefeng.guns.service.cinema.ConditionService;
import com.stylefeng.guns.service.cinema.vo.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("cinema")
public class CinemaController {

    @Reference(interfaceClass = CinemaService.class,check = false)
    CinemaService cinemaService;

    @Reference(interfaceClass = ConditionService.class,check = false)
    ConditionService conditionService;

    /**
     *  获取影院信息
     * @param cinemasReqVo
     * @return
     */
    @RequestMapping("getCinemas")
    public RespVo getCinemas(CinemasReqVo cinemasReqVo){
        RespVo respVo = null;
        try {
            respVo = cinemaService.getCinemas(cinemasReqVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new RespVo(999,"系统繁忙，请联系管理员");
        }
        return respVo;
    }

    @RequestMapping("getFieldInfo")
    public RespVo getFieldInfo(Integer fieldId,Integer cinemaId){
        return cinemaService.getFieldInfo(fieldId,cinemaId);
    }

    @RequestMapping("getFields")
    public Resp3Vo getFields(Integer cinemaId){
        Resp3Vo resp3Vo = new Resp3Vo();
        CinemaGetFieldsVo fileds = null;
        try {
            fileds = cinemaService.getFileds(cinemaId);
        } catch (Exception e) {
//            e.printStackTrace();
            resp3Vo.setStatus(1);
            resp3Vo.setMsg("查询失败");
            return resp3Vo;
        }
        resp3Vo.setData(fileds);
        resp3Vo.setImgPre("http://img.meetingshop.cn/");
        resp3Vo.setStatus(0);
        return resp3Vo;
    }

    @RequestMapping("getCondition")
    public RespVo getCondition(CinemaCondition cinemaCondition){
        RespVo respVo = conditionService.getCondition(cinemaCondition);
        return respVo;
    }


    @RequestMapping("isTrue")
    public Boolean isTrue(Integer fieldId,String seatId){
        Boolean trueSeats = cinemaService.isTrueSeats(fieldId, seatId);
        return trueSeats;
    }
}
