package com.stylefeng.guns.rest.modular.cinema.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.rest.common.exception.BizExceptionEnum;
import com.stylefeng.guns.service.cinema.CinemaService;
import com.stylefeng.guns.service.cinema.vo.CinemasDataVo;
import com.stylefeng.guns.service.cinema.vo.CinemasReqVo;
import com.stylefeng.guns.service.cinema.vo.RespVo;
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
            //e.printStackTrace();
            return new RespVo(999,"系统繁忙，请联系管理员");
        }
        return respVo;
    }

    @RequestMapping("getFieldInfo")
    public RespVo getFieldInfo(Integer fieldId,Integer cinemaId){
        return cinemaService.getFieldInfo(fieldId,cinemaId);
    }
}
