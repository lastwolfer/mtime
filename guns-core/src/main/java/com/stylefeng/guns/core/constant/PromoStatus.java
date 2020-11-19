package com.stylefeng.guns.core.constant;

import java.io.Serializable;

/**
 * @author: jia.xue
 * @create: 2019-08-06 16:22
 * @Description
 **/
public class PromoStatus extends BaseType implements Serializable {
    private static final long serialVersionUID = -1902713686357252305L;

    public PromoStatus(Integer index, String description) {
        super(index, description);
    }

    public static PromoStatus NOT_STATR = new PromoStatus(0,"未开始");

    public static PromoStatus ING = new PromoStatus(1,"进行中");

    public static PromoStatus ENDED = new PromoStatus(2,"已结束");


}