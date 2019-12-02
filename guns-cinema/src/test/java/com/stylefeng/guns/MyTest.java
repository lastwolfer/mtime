package com.stylefeng.guns;


import com.stylefeng.guns.rest.common.persistence.dao.MtimeFieldTMapper;
import com.stylefeng.guns.rest.service.impl.CinemaServiceImpl;
import com.stylefeng.guns.service.cinema.vo.CinemaGetFieldsVo;
import com.stylefeng.guns.service.cinema.vo.CinemaGetFieldsVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
public class MyTest {
    @Test
    public void fun(){
        CinemaServiceImpl mtimeCinemaTService = new CinemaServiceImpl();
        CinemaGetFieldsVo fileds = mtimeCinemaTService.getFileds(1);
        System.out.println(fileds);
    }
}
