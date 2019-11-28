package com.stylefeng.guns;


import com.stylefeng.guns.cinema.controller.CinemaGetFieldsVO;
import com.stylefeng.guns.cinema.service.impl.MtimeCinemaTServiceImpl;
import org.junit.Test;

public class MyTest {

    @Test
    public void fun(){
        MtimeCinemaTServiceImpl mtimeCinemaTService = new MtimeCinemaTServiceImpl();
        CinemaGetFieldsVO fileds = mtimeCinemaTService.getFileds(1);
        System.out.println(fileds);
    }
}
