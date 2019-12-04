package com.stylefeng.guns.service.order;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.service.film.vo.BaseRespVo;
import com.stylefeng.guns.service.order.vo.MoocOrder;
import org.springframework.stereotype.Component;

@Component
@Service
public interface OrderService {
    BaseRespVo buyTickets(Integer fieldId, String soldSeats, String seatsName, Integer userId);

    BaseRespVo getOrderInfo(Integer nowPage, Integer pageSize, Integer uuid);

    Boolean isSoldSeats(Integer filedId,String seatId);

    String hasSoldSeatIds(Integer filedId);

    MoocOrder getOrderById(String id);

//    String getSeatsIdsByFieldId(Integer fieldId);
}
