package com.stylefeng.guns.service.alipay.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Da
 * @version 1.0
 * @date 2019/12/03
 * @time 17:39
 */

@Data
public class BaseVoFromCheck implements Serializable {
    private static final long serialVersionUID = 7480016539722783311L;
    private Integer status;
    private CheckDetail data;
    private String msg;
}
