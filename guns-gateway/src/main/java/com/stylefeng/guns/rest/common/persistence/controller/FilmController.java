package com.stylefeng.guns.rest.common.persistence.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.service.cinema.vo.RespVo;
import com.stylefeng.guns.service.film.FilmService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("film")
public class FilmController {
    @Reference(check = false)
    FilmService filmService;

    @RequestMapping("getIndex")
    public RespVo getIndex(){
        RespVo index = filmService.getIndex();
        return index;
    }
}
