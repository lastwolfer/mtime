package com.stylefeng.guns.service.seckill;

import com.stylefeng.guns.service.film.vo.BaseRespVo;
import com.stylefeng.guns.service.seckill.vo.PublishPromoStockVo;

public interface SeckillService {
    public PublishPromoStockVo publishPromoStock();

    public BaseRespVo getPromo(Integer brandId, Integer hallType, Integer areaId, Integer pageSize, Integer nowPage);
}
