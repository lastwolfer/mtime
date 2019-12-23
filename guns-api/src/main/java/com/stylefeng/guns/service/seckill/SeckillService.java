package com.stylefeng.guns.service.seckill;

import com.stylefeng.guns.service.film.vo.BaseRespVo;
import com.stylefeng.guns.service.seckill.vo.CreateOrderVo;
import com.stylefeng.guns.service.seckill.vo.PromoOrderVO;
import com.stylefeng.guns.service.seckill.vo.PublishPromoStockVo;

public interface SeckillService {
    public PublishPromoStockVo publishPromoStock();

    public BaseRespVo getPromo(Integer brandId, Integer hallType, Integer areaId, Integer pageSize, Integer nowPage);

    public CreateOrderVo createOrder(Integer promoId, Integer amount, String promoToken, Integer uuid);

    String initPromoStockLog(Integer promoId, Integer amount);

    Boolean savePromoOrderInTransaction(Integer promoId, Integer valueOf, Integer amount, String stockLogId);

    PromoOrderVO savePromoOrderVO(Integer promoId, Integer userId, Integer amount, String stockLogId);

    String generateToken(Integer promoId, Integer valueOf);
}
