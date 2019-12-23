package com.stylefeng.guns.service.seckill.vo;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
@Data
public class ResponseVO implements Serializable {
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

    public static ResponseVO ok(){
        ResponseVO createOrderVo = new ResponseVO();
        createOrderVo.setMsg("下单成功！");
        createOrderVo.setStatus(0);
        return createOrderVo;
    }
    public static ResponseVO ok(String msg){
        ResponseVO createOrderVo = new ResponseVO();
        createOrderVo.setMsg(msg);
        createOrderVo.setStatus(0);
        return createOrderVo;
    }
    public static ResponseVO fail(String msg){
        ResponseVO createOrderVo = new ResponseVO();
        createOrderVo.setMsg(msg);
        createOrderVo.setStatus(0);
        return createOrderVo;
    }

    public static ResponseVO expire() {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setMsg("未登陆");
        return responseVO;
    }
}
