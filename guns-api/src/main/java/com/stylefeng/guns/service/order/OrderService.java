package com.stylefeng.guns.service.order;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.service.film.vo.BaseRespVo;
import com.stylefeng.guns.service.order.vo.OrderVo;
import org.springframework.stereotype.Component;

public interface OrderService {
    BaseRespVo buyTickets(Integer fieldId, String soldSeats, String seatsName, Integer userId);

    BaseRespVo getOrderInfo(Integer nowPage, Integer pageSize, Integer uuid);

    String getSeatsIdsByFieldId(Integer fieldId);
}
