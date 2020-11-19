package com.stylefeng.guns.core.constant;

/**
 * @author: jia.xue
 * @create: 2019-08-05 17:49
 * @Description
 **/
public class WalletOperType extends BaseType {
    public WalletOperType(Integer index, String description) {
        super(index, description);
    }

    public static WalletOperType SUBSTRACT = new WalletOperType(1,"扣费");
    public static WalletOperType  ADD= new WalletOperType(2,"加钱");
}