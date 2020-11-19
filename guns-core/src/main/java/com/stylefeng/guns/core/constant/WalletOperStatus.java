package com.stylefeng.guns.core.constant;

/**
 * @author: jia.xue
 * @create: 2019-08-05 17:51
 * @Description
 **/
public class WalletOperStatus extends BaseType {
    public WalletOperStatus(Integer index, String description) {
        super(index, description);
    }
    public static WalletOperStatus INIT = new WalletOperStatus(0,"初始化");
    public static WalletOperStatus SUCCESS = new WalletOperStatus(1,"操作成功");
    public static WalletOperStatus FAIL = new WalletOperStatus(2,"操作失败");

}