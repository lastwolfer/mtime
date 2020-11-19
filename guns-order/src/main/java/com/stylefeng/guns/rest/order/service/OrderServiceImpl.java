package com.stylefeng.guns.rest.order.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
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
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    MoocOrderTMapper moocOrderTMapper;

    @Reference(interfaceClass = CinemaService.class, check = false)
    CinemaService cinemaService;

    @Reference(interfaceClass = FilmService.class, check = false)
    FilmService filmService;

    @Override
    public BaseRespVo buyTickets(Integer fieldId, String soldSeats, String seatsName, Integer userId) {
        //第二步：看看购买的座位是否已被购买,防止出现卖重了
        //根据filed_id;status = 1,取出该场次所有已售出订单
        EntityWrapper<MoocOrderT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("order_status",1);
        entityWrapper.eq("field_id",fieldId+"");
        List<MoocOrderT> moocOrderTS = moocOrderTMapper.selectList(entityWrapper);
        //遍历ids进行比较
        for (MoocOrderT moocOrderT : moocOrderTS) {
            String[] seatIds = moocOrderT.getSeatsIds().split(",");
            for (String seatId : seatIds) {
                if(seatId.equals(soldSeats)){
                    return null;
                }
            }
        }
        //第三步：校验好参数后，往订单数据库mooc_order_t里插入数据
        MoocOrderT moocOrderT = new MoocOrderT();
        //生成订单的uuid
        String uuid = UUID.randomUUID().toString();
        moocOrderT.setUuid(uuid);
        //根据fieldId获取影厅相关信息
        FieldInfoForOrderVo field = cinemaService.getOrderField(fieldId);
        //设置影院id
        moocOrderT.setCinemaId(field.getCinemaId());
        //设置影厅id
        moocOrderT.setFieldId(fieldId);
        //设置影片id
        moocOrderT.setFilmId(field.getFilmId());
        //设置售出的座位号
        moocOrderT.setSeatsIds(soldSeats);
        //设置售出的座位类型
        moocOrderT.setSeatsName(seatsName);
        //设置单价
        moocOrderT.setFilmPrice(Double.valueOf(field.getPrice()));
        //判断票数，设置总价：票数*单价
        int number = soldSeats.split(",").length;
        moocOrderT.setOrderPrice((double) (number * field.getPrice()));
        //设置生成订单时间
        moocOrderT.setOrderTime(new Date());
        //设置订单用户Id
        moocOrderT.setOrderUser(userId);
        //设置订单状态id
        moocOrderT.setOrderStatus(0);//0表示未支付
        //设置完毕，插入数据
        Integer insert = moocOrderTMapper.insert(moocOrderT);
        //失败情况
        if (insert == 0) {
            BaseRespVo<Object> baseRespVo = new BaseRespVo<>();
            baseRespVo.setMsg("该订单不存在");
            baseRespVo.setStatus(1);
            return baseRespVo;
        }
        //拼接响应报文
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderId(uuid);//设置orderId
        String filmName = filmService.getFilmNameById(field.getFilmId());
        orderVo.setFilmName(filmName);//设置电影名
        long time = System.currentTimeMillis() / 1000;
        String filmTime = new SimpleDateFormat("yy年MM月dd日 ").format(time);
        orderVo.setFieldTime(filmTime+ field.getBeginTime());//设置电影开始时间 当天年月日+当前时间
        CinemaInfoVo cinema = cinemaService.getCinemaById(field.getCinemaId());
        orderVo.setCinemaName(cinema.getCinemaName());//设置影院名
        orderVo.setSeatsName(seatsName);//设置座位类型
        orderVo.setOrderPrice(String.valueOf(moocOrderT.getOrderPrice()));//设置订单总价
        orderVo.setOrderTimestamp(String.valueOf(time));//设置当前时间（毫秒数）
        return BaseRespVo.ok(orderVo);
    }

    /**
     * 获取订单信息
     *
     * @param nowPage
     * @param pageSize
     * @param userId
     * @return
     */
    @Override
    public BaseRespVo getOrderInfo(Integer nowPage, Integer pageSize, Integer userId) {
        Page<Object> page = new Page<>(nowPage, pageSize);
        EntityWrapper<MoocOrderT> wrapper = new EntityWrapper<>();
        wrapper.eq("order_user", userId);
        List<MoocOrderT> moocOrderTS = moocOrderTMapper.selectPage(page, wrapper);
        if (CollectionUtils.isEmpty(moocOrderTS)) {
            return BaseRespVo.fail(1, "订单列表为空哦~~");
        }
        List<OrderVo> orderVos = new ArrayList<>();
        for (MoocOrderT moocOrderT : moocOrderTS) {
            OrderVo orderVo = new OrderVo();
            BeanUtils.copyProperties(moocOrderT, orderVo);
            String filmName = filmService.getFilmNameById(moocOrderT.getFilmId());
            orderVo.setFilmName(filmName);
            Integer fieldId = moocOrderT.getFieldId();
            FieldInfoForOrderVo orderField = cinemaService.getOrderField(fieldId);
            Date orderTime = moocOrderT.getOrderTime();
            String filmTime = new SimpleDateFormat("yy年MM月dd日 ").format(orderTime);
            orderVo.setFieldTime(filmTime + orderField.getBeginTime());
            CinemaInfoVo cinema = cinemaService.getCinemaById(moocOrderT.getCinemaId());
            orderVo.setCinemaName(cinema.getCinemaName());
            orderVo.setOrderId(moocOrderT.getUuid());
            orderVo.setOrderPrice(String.valueOf(moocOrderT.getOrderPrice()));
            orderVo.setOrderTimestamp(String.valueOf(orderTime.getTime() / 1000));
            if (moocOrderT.getOrderStatus() == 0) {
                orderVo.setOrderStatus("未支付");
            }
            if (moocOrderT.getOrderStatus() == 1) {
                orderVo.setOrderStatus("已完成");
            }
            if (moocOrderT.getOrderStatus() == 2) {
                orderVo.setOrderStatus("已关闭");
            }
            orderVos.add(orderVo);
        }
        return BaseRespVo.ok(orderVos);
    }

    @Override
    public String getSeatsIdsByFieldId(Integer fieldId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("field_id", fieldId).ne("order_status", 2);
        List list = moocOrderTMapper.selectList(wrapper);
        return null;
    }
}
