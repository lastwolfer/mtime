package com.stylefeng.guns.rest.seckill.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoStockMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromo;
import com.stylefeng.guns.service.cinema.CinemaService;
import com.stylefeng.guns.service.cinema.vo.CinemasDataVo;
import com.stylefeng.guns.service.cinema.vo.CinemasReqVo;
import com.stylefeng.guns.service.cinema.vo.RespVo;
import com.stylefeng.guns.service.film.vo.BaseRespVo;
import com.stylefeng.guns.service.seckill.SeckillService;
import com.stylefeng.guns.service.seckill.vo.PublishPromoStockVo;
import com.stylefeng.guns.service.seckill.vo.SeckillVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Service(interfaceClass = SeckillService.class)
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    MtimePromoMapper promoMapper;
    @Autowired
    MtimePromoStockMapper promoStockMapper;
    @Override
    public PublishPromoStockVo publishPromoStock() {
        PublishPromoStockVo publishPromoStockVo = new PublishPromoStockVo();
        publishPromoStockVo.setMsg("发布成功");
        publishPromoStockVo.setData("");
        publishPromoStockVo.setTotalPage("");
        publishPromoStockVo.setImgPre("");
        publishPromoStockVo.setNowPage("");
        publishPromoStockVo.setStatus(0);
        return publishPromoStockVo;
    }
    @Reference(interfaceClass = CinemaService.class,check = false)
    CinemaService cinemaService;
    @Override
    public BaseRespVo getPromo(Integer brandId, Integer hallType, Integer areaId, Integer pageSize, Integer nowPage) {
        CinemasReqVo cinemasReqVo = new CinemasReqVo();
        cinemasReqVo.setBrandId(brandId);
        cinemasReqVo.setHallType(hallType);
        cinemasReqVo.setAreaId(areaId);
        cinemasReqVo.setPageSize(pageSize);
        cinemasReqVo.setNowPage(nowPage);
        RespVo respVo = cinemaService.getCinemas(cinemasReqVo);
        List<CinemasDataVo> data = (List<CinemasDataVo>) respVo.getData();

        List<SeckillVo> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(data)){
            for (CinemasDataVo datum : data) {
                SeckillVo seckillVo = new SeckillVo();
                seckillVo.setCinemaId(datum.getUuid());
                seckillVo.setCinemaName(datum.getCinemaName());
                Random random = new Random();
                int i = random.nextInt(6);
                seckillVo.setImgAddress("cinema"+(i+1)+".jpg");
                seckillVo.setCinemaAddress(datum.getCinemaAddress());

                EntityWrapper<MtimePromo> wrapper = new EntityWrapper<>();
                wrapper.eq("cinema_id",datum.getUuid());
                List<MtimePromo> mtimePromos = promoMapper.selectList(wrapper);
                if(!CollectionUtils.isEmpty(mtimePromos)){
                    MtimePromo mtimePromo = mtimePromos.get(0);
                    seckillVo.setDescription(mtimePromo.getDescription());
                    seckillVo.setPrice(mtimePromo.getPrice().doubleValue());
                    seckillVo.setStartTime(mtimePromo.getStartTime()+"");
                    seckillVo.setEndTime(mtimePromo.getEndTime()+"");
                    seckillVo.setUuid(mtimePromo.getUuid());
                    seckillVo.setStatus(mtimePromo.getStatus());
                    Integer stock=promoStockMapper.selectStockById(mtimePromo.getUuid());
                    seckillVo.setStock(stock);
                    list.add(seckillVo);
                }
            }
        }
        BaseRespVo ok = BaseRespVo.ok(list);
        return ok;
    }
}
