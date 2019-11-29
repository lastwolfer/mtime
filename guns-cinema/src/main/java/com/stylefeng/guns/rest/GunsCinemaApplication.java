<<<<<<< HEAD:guns-cinema/src/main/java/com/stylefeng/guns/rest/user/GunsCinemaApplication.java
package com.stylefeng.guns.rest.user;
=======
package com.stylefeng.guns.rest;
>>>>>>> 951ff2a3c62298faf2075002f6dc26d7eb33f358:guns-cinema/src/main/java/com/stylefeng/guns/rest/GunsCinemaApplication.java

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.stylefeng.guns"})
@MapperScan(basePackages = "com.stylefeng.guns.rest.common.persistence.dao")
@EnableDubboConfiguration
public class GunsCinemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GunsCinemaApplication.class, args);
    }
}
