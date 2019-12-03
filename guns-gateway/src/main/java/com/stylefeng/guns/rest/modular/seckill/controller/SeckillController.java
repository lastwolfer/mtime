package com.stylefeng.guns.rest.modular.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.service.film.vo.BaseRespVo;
import com.stylefeng.guns.service.seckill.SeckillService;
import com.stylefeng.guns.service.seckill.vo.PublishPromoStockVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeckillController {

    @Reference(interfaceClass = SeckillService.class,check = false)
    SeckillService seckillService;

    @RequestMapping("promo/publishPromoStock")
    public PublishPromoStockVo publishPromoStock() {
        PublishPromoStockVo publishPromoStockVo = seckillService.publishPromoStock();
        return publishPromoStockVo;
    }
    @RequestMapping("promo/getPromo")
    public BaseRespVo getPromo(Integer brandId, Integer hallType, Integer areaId, Integer pageSize, Integer nowPage) {
        BaseRespVo promo = seckillService.getPromo(brandId, hallType, areaId, pageSize, nowPage);
        return promo;
    }

}
