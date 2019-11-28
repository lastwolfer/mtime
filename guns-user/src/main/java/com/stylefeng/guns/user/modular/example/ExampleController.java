package com.stylefeng.guns.user.modular.example;

<<<<<<< HEAD
import com.stylefeng.guns.user.common.SimpleObject;
=======
import com.stylefeng.guns.user.common.persistence.SimpleObject;
>>>>>>> ae0ef5851c0013e2b349f4f0b258a5e79cc62d05
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 常规控制器
 *
 * @author fengshuonan
 * @date 2017-08-23 16:02
 */
@Controller
@RequestMapping("/hello")
public class ExampleController {

    @RequestMapping("")
    public ResponseEntity hello(@RequestBody SimpleObject simpleObject) {
        System.out.println(simpleObject.getUser());
        return ResponseEntity.ok("请求成功!");
    }
}
