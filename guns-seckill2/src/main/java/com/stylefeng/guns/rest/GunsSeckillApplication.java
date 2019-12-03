package com.stylefeng.guns.rest;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.stylefeng.guns"})
@MapperScan("com.stylefeng.guns.rest.common.persistence")
@EnableDubboConfiguration
public class GunsSeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(GunsSeckillApplication.class, args);
    }
}
