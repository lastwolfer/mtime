package com.stylefeng.guns.cinema.controller;



import com.stylefeng.guns.cinema.service.IMtimeCinemaTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 影院信息表 前端控制器
 * </p>
 *
 * @author pandax
 * @since 2019-11-28
 */
@RestController
public class MtimeCinemaTController {

    @Autowired
    IMtimeCinemaTService mtimeCinemaTService;

    @RequestMapping("cinema/getFields")
    public CinemaGetFieldsVO getFields(Integer id){
        return mtimeCinemaTService.getFileds(id);
    }
}

