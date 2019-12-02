package com.stylefeng.guns.service.alipay.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Da
 * @version 1.0
 * @date 2019/12/02
 * @time 21:44
 */

@Data
public class BaseVo implements Serializable {
    private static final long serialVersionUID = -2336622660434139836L;
    private PayDetail data;
    private String imgPre;
    private Integer status;
}
