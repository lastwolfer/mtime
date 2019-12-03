package com.stylefeng.guns.rest.modular.alipay;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.service.alipay.AlipayService;
import com.stylefeng.guns.service.alipay.vo.BaseVo;
import com.stylefeng.guns.service.alipay.vo.BaseVoFromCheck;
import com.stylefeng.guns.service.alipay.vo.CheckDetail;
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

    /**
     * 用支付宝当面付支付
     * @param orderId
     * @return
     */
    @RequestMapping("order/getPayInfo")
    public BaseVo pay(String orderId){
        String pay = alipayService.pay(orderId);
        BaseVo baseVo = new BaseVo();
        PayDetail payDetail = new PayDetail();
        payDetail.setOrderId(orderId);
        payDetail.setQRCodeAddress(pay);
        baseVo.setData(payDetail);
        baseVo.setImgPre("https://picture-agang-1300811584.cos.ap-beijing.myqcloud.com/");
        baseVo.setStatus(0);
        return baseVo;
    }

    @RequestMapping("order/getPayResult")
    public BaseVoFromCheck getPayResult(String orderId, Integer tryNums){
        Integer status = alipayService.check(orderId);
        BaseVoFromCheck baseVoFromCheck = new BaseVoFromCheck();
        CheckDetail data = new CheckDetail();
        if(tryNums > 3) {
            baseVoFromCheck.setStatus(1);
            baseVoFromCheck.setMsg("支付超时，请重试");
        }
        if(status == 0){
            baseVoFromCheck.setStatus(0);
            data.setOrderId(orderId);
            data.setOrderMsg("支付成功");
            data.setOrderStatus(1);
            baseVoFromCheck.setData(data);
        } else {
            baseVoFromCheck.setStatus(2);
        }
        return baseVoFromCheck;
    }
}
