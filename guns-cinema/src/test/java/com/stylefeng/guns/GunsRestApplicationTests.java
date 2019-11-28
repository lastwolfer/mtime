package com.stylefeng.guns;

import com.stylefeng.guns.cinema.controller.CinemaGetFieldsVO;
import com.stylefeng.guns.cinema.service.impl.MtimeCinemaTServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootConfiguration
@SpringBootTest
@MapperScan(basePackages = "com.stylefeng.guns.cinema.persistence.dao")
public class GunsRestApplicationTests {

	@Test
	public void contextLoads() {
		MtimeCinemaTServiceImpl mtimeCinemaTService = new MtimeCinemaTServiceImpl();
		CinemaGetFieldsVO fileds = mtimeCinemaTService.getFileds(1);
		System.out.println(fileds);
	}

}
