package com.stylefeng.guns.service.promo;

import com.stylefeng.guns.service.cinema.vo.RespVo;
import com.stylefeng.guns.service.promo.vo.PromoConditionVo;
import com.stylefeng.guns.service.promo.vo.PromoVo;


public interface PromoService {
    RespVo getPromo(PromoConditionVo promoConditionVo);
    //普通的生成秒杀订单，扣减库存，扣钱
    PromoVo createOrder(Integer promoId, Integer amount, Integer userId,String stockLogId);
    //初始化库存流水
    String initPromoStockLog(Integer promoId,Integer amount);
    //发布到redis缓存中
    Boolean publishPromoStock();
    //事务型生成秒杀订单
    Boolean savePromoOrderInTransaction(Integer promoId, Integer amount, Integer userId,String stockLogId);
    //生成秒杀令牌
    String generateToken(Integer promoId,Integer userId);
}
