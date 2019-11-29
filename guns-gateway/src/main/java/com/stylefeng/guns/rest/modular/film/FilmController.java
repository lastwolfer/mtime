package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.service.film.FilmService;
import com.stylefeng.guns.service.film.vo.BaseRespVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FilmController {
    @Reference(interfaceClass = FilmService.class,check = false)
    FilmService filmService;
    @RequestMapping("film/getIndex")
    public BaseRespVo getIndex(){
        Map<String, Object> map = filmService.getIndex();
        return BaseRespVo.ok(map);
    }
    @RequestMapping("film/getConditionList")
    public BaseRespVo getConditionList(Integer catId,Integer sourceId,Integer yearId){
        Map<String, Object> condition = filmService.getCondition(catId, sourceId, yearId);
        return BaseRespVo.ok(condition);
    }
}
