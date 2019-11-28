package com.stylefeng.guns.fastjson;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.core.util.MD5Util;
<<<<<<< HEAD
import com.stylefeng.guns.user.common.SimpleObject;
=======
import com.stylefeng.guns.user.common.persistence.SimpleObject;
>>>>>>> ae0ef5851c0013e2b349f4f0b258a5e79cc62d05
import com.stylefeng.guns.user.modular.auth.converter.BaseTransferEntity;

/**
 * json测试
 *
 * @author fengshuonan
 * @date 2017-08-25 16:11
 */


public class JsonTest {

    public static void main(String[] args) {
        String randomKey = "1xm7hw";

        BaseTransferEntity baseTransferEntity = new BaseTransferEntity();
        SimpleObject simpleObject = new SimpleObject();
        simpleObject.setUser("fsn");
        baseTransferEntity.setObject("123123");

        String json = JSON.toJSONString(simpleObject);

        //md5签名
        String encrypt = MD5Util.encrypt(json + randomKey);
        baseTransferEntity.setSign(encrypt);

        System.out.println(JSON.toJSONString(baseTransferEntity));
    }
}
