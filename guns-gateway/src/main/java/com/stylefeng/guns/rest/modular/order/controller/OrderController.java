package com.stylefeng.guns.rest.modular.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.stylefeng.guns.service.order.MoocOrderTService;
import com.stylefeng.guns.service.order.vo.BaseVo;
import com.stylefeng.guns.service.order.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("order")
public class OrderController {

    @Reference(interfaceClass = MoocOrderTService.class)
    MoocOrderTService moocOrderTService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @RequestMapping("buyTickets")
    public BaseVo buyTickets(Integer fieldId,String soldSeats,String seatsName){
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String authorization = request.getHeader("Authorization");
//        String authToken = authorization.substring(7);
//        String userName = jwtTokenUtil.getUsernameFromToken(authToken);
//        OrderVo orderVo = moocOrderTService.buyTickets(fieldId,soldSeats,seatsName,userName);
        return null;
    }

}
