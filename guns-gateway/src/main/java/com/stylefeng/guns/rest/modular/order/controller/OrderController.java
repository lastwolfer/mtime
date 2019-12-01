package com.stylefeng.guns.rest.modular.order.controller;

import com.stylefeng.guns.service.order.vo.BaseVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
public class OrderController {

    @RequestMapping("buyTickets")
    public BaseVo buyTickets(Integer fieldId,Integer soldSeats,String searsName){
        return null;
    }

}
