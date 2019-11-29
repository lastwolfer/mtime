package com.stylefeng.guns.service.film.vo.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/29
 * @time 15:00
 */

@Data
public class BaseVo implements Serializable {
    private static final long serialVersionUID = 4348689430491988297L;
    private Integer status;
    private String msg;

    public static BaseVo fail(Integer status, String msg){
        BaseVo baseVo = new BaseVo();
        baseVo.setStatus(status);
        baseVo.setMsg(msg);

        return baseVo;
    }
}
