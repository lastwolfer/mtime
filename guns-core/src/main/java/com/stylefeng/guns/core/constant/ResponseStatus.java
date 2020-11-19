package com.stylefeng.guns.core.constant;

/**
 * @author: jia.xue
 * @create: 2019-06-11 20:28
 * @Description
 **/
public class ResponseStatus extends BaseType{

    public static ResponseStatus success = new ResponseStatus(0,"success");
    public static ResponseStatus fail = new ResponseStatus(1,"fail");
    public static ResponseStatus exception = new ResponseStatus(999,"exception");
    public static ResponseStatus expire = new ResponseStatus(700,"expire");

    public ResponseStatus(Integer index, String description) {
        super(index, description);
    }


}