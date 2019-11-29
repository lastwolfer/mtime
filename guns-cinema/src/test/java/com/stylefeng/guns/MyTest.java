package com.stylefeng.guns;


import com.stylefeng.guns.rest.service.impl.CinemaServiceImpl;
import com.stylefeng.guns.service.cinema.vo.CinemaGetFieldsVO;
import org.junit.Test;

public class MyTest {

    @Test
    public void fun(){
        CinemaServiceImpl mtimeCinemaTService = new CinemaServiceImpl();
        CinemaGetFieldsVO fileds = mtimeCinemaTService.getFileds(1);
        System.out.println(fileds);
    }
}
