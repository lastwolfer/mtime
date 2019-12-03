package com.stylefeng.guns.service.alipay.vo;

import lombok.Data;

/**
 * @author Da
 * @version 1.0
 * @date 2019/12/03
 * @time 17:40
 */

@Data
public class CheckDetail {
    private String orderId;
    private String orderMsg;
    private Integer orderStatus;
}
