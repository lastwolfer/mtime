<<<<<<< HEAD:guns-rest/src/main/java/com/stylefeng/guns/rest/user/GunsRestServletInitializer.java
package com.stylefeng.guns.rest.user;
=======
package com.stylefeng.guns.rest;
>>>>>>> 951ff2a3c62298faf2075002f6dc26d7eb33f358:guns-rest/src/main/java/com/stylefeng/guns/rest/GunsRestServletInitializer.java

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Guns REST Web程序启动类
 *
 * @author fengshuonan
 * @date 2017年9月29日09:00:42
 */
public class GunsRestServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(GunsRestApplication.class);
    }

}
