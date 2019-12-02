package com.stylefeng.guns.rest.order.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.service.cinema.CinemaService;
import com.stylefeng.guns.service.cinema.vo.CinemaInfoVo;
import com.stylefeng.guns.service.cinema.vo.FieldInfoForOrderVo;
import com.stylefeng.guns.service.film.FilmService;
import com.stylefeng.guns.service.film.vo.BaseRespVo;
import com.stylefeng.guns.service.order.OrderService;
import com.stylefeng.guns.service.order.vo.OrderVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OrderServiceImpl implements OrderService {

    @Autowired
    MoocOrderTMapper moocOrderTMapper;
    @Autowired
    CinemaService cinemaService;
    @Autowired
    FilmService filmService;

    @Override
    public BaseRespVo buyTickets(Integer fieldId, String soldSeats, String seatsName, Integer userId) {
        //先将数据存进去
        String uuid = UUID.randomUUID().toString();
        MoocOrderT moocOrderT = new MoocOrderT();
        moocOrderT.setUuid(uuid);
        FieldInfoForOrderVo field = cinemaService.getOrderField(fieldId);

        moocOrderT.setCinemaId(field.getCinemaId());
        moocOrderT.setFieldId(fieldId);
        moocOrderT.setFilmId(field.getFilmId());
        moocOrderT.setSeatsIds(soldSeats);
        moocOrderT.setSeatsName(seatsName);
        int number = soldSeats.split(",").length; //获取票数
        moocOrderT.setFilmPrice(Double.valueOf(field.getPrice()));
        moocOrderT.setOrderPrice((double) (number*field.getPrice()));
        moocOrderT.setOrderTime(new Date());
        moocOrderT.setOrderUser(userId);
        moocOrderT.setOrderStatus(0);//0表示未支付
        Integer insert = moocOrderTMapper.insert(moocOrderT);
        if (insert==0){
            BaseRespVo<Object> baseRespVo = new BaseRespVo<>();
            baseRespVo.setMsg("该订单不存在");
            baseRespVo.setStatus(1);
            return baseRespVo;
        }
        //拼接响应报文
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderId(uuid);
        String filmName = filmService.getFilmNameById(field.getFilmId());
        orderVo.setFilmName(filmName);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("今天 MM月dd号 HH:mm");
        String format = simpleDateFormat.format(new Date());
        orderVo.setFieldTime(format);
        CinemaInfoVo cinema = cinemaService.getCinemaById(field.getCinemaId());
        orderVo.setCinemaName(cinema.getCinemaName());
        orderVo.setSeatsName(seatsName);
        orderVo.setOrderPrice(String.valueOf(moocOrderT.getOrderPrice()));
        orderVo.setOrderTimestamp(String.valueOf(System.currentTimeMillis()));
        return BaseRespVo.ok(orderVo);
    }

    /**
     * 获取订单信息
     * @param nowPage
     * @param pageSize
     * @param userId
     * @return
     */
    @Override
    public BaseRespVo getOrderInfo(Integer nowPage, Integer pageSize, Integer userId) {
        Page<Object> page = new Page<>(nowPage,pageSize);
        EntityWrapper<MoocOrderT> wrapper = new EntityWrapper<>();
        wrapper.eq("order_user",userId);
        List<MoocOrderT> moocOrderTS = moocOrderTMapper.selectPage(page, wrapper);
        if (CollectionUtils.isEmpty(moocOrderTS)){
            return BaseRespVo.fail(1,"订单列表为空哦~~");
        }
        List<OrderVo> orderVos = new ArrayList<>();
        for (MoocOrderT moocOrderT : moocOrderTS) {
            OrderVo orderVo = new OrderVo();
            BeanUtils.copyProperties(moocOrderT,orderVo);
            String filmName = filmService.getFilmNameById(moocOrderT.getFilmId());
            orderVo.setFilmName(filmName);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("今天 MM月dd号 HH:mm");
            String format = simpleDateFormat.format(new Date());
            orderVo.setFieldTime(format);
            CinemaInfoVo cinema = cinemaService.getCinemaById(moocOrderT.getCinemaId());
            orderVo.setCinemaName(cinema.getCinemaName());
            if (moocOrderT.getOrderStatus()==0){
                orderVo.setOrderStatus("未支付");
            }
            if (moocOrderT.getOrderStatus()==1){
                orderVo.setOrderStatus("已完成");
            }
            if (moocOrderT.getOrderStatus()==2){
                orderVo.setOrderStatus("已关闭");
            }
            orderVos.add(orderVo);
        }
        return BaseRespVo.ok(orderVos);
    }

    @Override
    public String getSeatsIdsByFieldId(Integer fieldId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("field_id",fieldId).ne("order_status",2);
        List list = moocOrderTMapper.selectList(wrapper);
        list.get
        return null;
    }
}
