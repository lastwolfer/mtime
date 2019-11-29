<<<<<<< HEAD:guns-rest/src/main/java/com/stylefeng/guns/rest/user/GunsRestApplication.java
package com.stylefeng.guns.rest.user;
=======
package com.stylefeng.guns.rest;
>>>>>>> 951ff2a3c62298faf2075002f6dc26d7eb33f358:guns-rest/src/main/java/com/stylefeng/guns/rest/GunsRestApplication.java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.stylefeng.guns"})
public class GunsRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(GunsRestApplication.class, args);
    }
}
