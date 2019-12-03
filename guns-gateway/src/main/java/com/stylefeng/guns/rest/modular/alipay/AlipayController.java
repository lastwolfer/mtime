package com.stylefeng.guns.rest.modular.alipay;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.service.alipay.AlipayService;
import com.stylefeng.guns.service.alipay.vo.BaseVo;
import com.stylefeng.guns.service.alipay.vo.PayDetail;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Da
 * @version 1.0
 * @date 2019/12/02
 * @time 21:41
 */

@RestController
public class AlipayController {

    @Reference(interfaceClass = AlipayService.class, check = false)
    AlipayService alipayService;

    @RequestMapping("order/getPayInfo")
    public BaseVo pay(String orderId){
        String pay = alipayService.pay(orderId);
        BaseVo baseVo = new BaseVo();
        PayDetail payDetail = new PayDetail();
        payDetail.setOrderId(orderId.toString());
        payDetail.setQRCodeAddress(pay);
        baseVo.setData(payDetail);
        baseVo.setImgPre("https://picture-agang-1300811584.cos.ap-beijing.myqcloud.com/");
        baseVo.setStatus(0);
        return baseVo;
    }
}
