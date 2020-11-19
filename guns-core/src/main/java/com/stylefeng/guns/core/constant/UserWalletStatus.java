package com.stylefeng.guns.core.constant;

import org.apache.catalina.User;

/**
 * @author: jia.xue
 * @create: 2019-08-05 11:33
 * @Description
 **/
public class UserWalletStatus extends BaseType {

    public UserWalletStatus(Integer index, String description) {
        super(index, description);
    }

    public static UserWalletStatus NORMAL = new UserWalletStatus(1,"正常");
    public static UserWalletStatus LOCKED = new UserWalletStatus(0,"锁定");



}