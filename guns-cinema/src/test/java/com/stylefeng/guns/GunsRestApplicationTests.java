package com.stylefeng.guns;

import com.stylefeng.guns.rest.service.impl.CinemaServiceImpl;
import com.stylefeng.guns.service.cinema.vo.CinemaGetFieldsVO;
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
		CinemaServiceImpl mtimeCinemaTService = new CinemaServiceImpl();
		CinemaGetFieldsVO fileds = mtimeCinemaTService.getFileds(1);
		System.out.println(fileds);
	}

}
