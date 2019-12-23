package com.stylefeng.guns.service.seckill.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreateOrderVo implements Serializable {

    private static final long serialVersionUID = 430288945314783543L;
    /**
     * msg : 下单成功！
     * data :
     * totalPage :
     * imgPre :
     * nowPage :
     * status : 0
     */
    private String msg;
    private String data="";
    private String totalPage="";
    private String imgPre="";
    private String nowPage="";
    private int status;

    public static CreateOrderVo ok(){
        CreateOrderVo createOrderVo = new CreateOrderVo();
        createOrderVo.setMsg("下单成功！");
        createOrderVo.setStatus(0);
        return createOrderVo;
    }
    public static CreateOrderVo fail(String msg){
        CreateOrderVo createOrderVo = new CreateOrderVo();
        createOrderVo.setMsg(msg);
        createOrderVo.setStatus(0);
        return createOrderVo;
    }
}
