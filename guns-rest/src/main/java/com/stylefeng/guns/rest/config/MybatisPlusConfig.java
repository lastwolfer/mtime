package com.stylefeng.guns.rest.config;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus配置
 *
 * @author stylefeng
 * @Date 2017年8月23日12:51:41
 */
@Configuration
<<<<<<< HEAD
@MapperScan(basePackages = {"com.stylefeng.guns.user.*.dao", "com.stylefeng.guns.user.common.persistence.dao"})
=======
@MapperScan(basePackages = {"com.stylefeng.guns.rest.*.dao", "com.stylefeng.guns.rest.common.persistence.dao"})
>>>>>>> 951ff2a3c62298faf2075002f6dc26d7eb33f358
public class MybatisPlusConfig {

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
