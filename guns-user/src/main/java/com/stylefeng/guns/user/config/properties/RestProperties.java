package com.stylefeng.guns.user.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 项目相关配置
 *
 * @author fengshuonan
 * @date 2017年10月23日16:44:15
 */
@Configuration
@ConfigurationProperties(prefix = RestProperties.REST_PREFIX)
public class RestProperties {

<<<<<<< HEAD
    public static final String REST_PREFIX = "rest";
=======
    public static final String REST_PREFIX = "user";
>>>>>>> ae0ef5851c0013e2b349f4f0b258a5e79cc62d05

    private boolean authOpen = true;

    private boolean signOpen = true;

    public boolean isAuthOpen() {
        return authOpen;
    }

    public void setAuthOpen(boolean authOpen) {
        this.authOpen = authOpen;
    }

    public boolean isSignOpen() {
        return signOpen;
    }

    public void setSignOpen(boolean signOpen) {
        this.signOpen = signOpen;
    }
}
