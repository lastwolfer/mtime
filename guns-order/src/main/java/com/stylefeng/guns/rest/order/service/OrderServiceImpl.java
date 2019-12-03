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
import com.stylefeng.guns.service.order.vo.MoocOrder;
import com.stylefeng.guns.service.order.vo.OrderVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

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
        //先将数据存进去
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
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
        moocOrderT.setOrderPrice((double) (number * field.getPrice()));
        moocOrderT.setOrderTime(new Date());
        moocOrderT.setOrderUser(userId);
        moocOrderT.setOrderStatus(0);//0表示未支付
        Integer insert = moocOrderTMapper.insert(moocOrderT);
        if (insert == 0) {
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
//        long time = System.currentTimeMillis() / 1000;
        Date time = new Date();
        String filmTime = new SimpleDateFormat("yyyy年MM月dd日 ").format(time);
        orderVo.setFieldTime(filmTime + field.getBeginTime());
        CinemaInfoVo cinema = cinemaService.getCinemaById(field.getCinemaId());
        orderVo.setCinemaName(cinema.getCinemaName());
        orderVo.setSeatsName(seatsName);
        orderVo.setOrderPrice(String.valueOf(moocOrderT.getOrderPrice()));
        orderVo.setOrderTimestamp(String.valueOf(time));
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

    /**
     * 根据场次id获取座位信息
     *
     * @param fieldId 场次id
     * @return
     */
    private String getSeatsIdsByFieldId(Integer fieldId) {
        EntityWrapper wrapper = new EntityWrapper();
        /*wrapper.eq("field_id",fieldId).ne("order_status",2);
        List list = moocOrderTMapper.selectList(wrapper);*/
        EntityWrapper<MoocOrderT> moocOrderTEntityWrapper = new EntityWrapper<>();
        moocOrderTEntityWrapper.eq("field_id", fieldId).ne("order_status", 2);
        List<MoocOrderT> list = moocOrderTMapper.selectList(moocOrderTEntityWrapper);
        StringBuilder sb = new StringBuilder();
        for (MoocOrderT moocOrderT : list) {
            sb.append(moocOrderT.getSeatsIds()).append(",");
        }
        //这个末尾有个逗号，后面匹配的时候有用
        return sb.toString();
    }

    /**
     * 判断下单的座位数据是否已经被选择
     *
     * @param filedId 场次id
     * @param seatId  座位id（s）
     * @return
     */
    @Override
    public Boolean isSoldSeats(Integer filedId, String seatId) {
        String seatIds = "," + getSeatsIdsByFieldId(filedId);
        String[] seatIdsFromClient = seatId.split(",");
        for (String seatID : seatIdsFromClient) {
            String need2jug = "," + seatID + ",";
            if (seatIds.contains(need2jug)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据场次id获得所有座位信息
     *
     * @param filedId
     * @return
     */
    @Override
    public String hasSoldSeatIds(Integer filedId) {
        String seatsIdsByFieldId = getSeatsIdsByFieldId(filedId);
        String seatIds = "";
        if (seatsIdsByFieldId!=null&&!seatsIdsByFieldId.trim().isEmpty()){
            seatIds = seatsIdsByFieldId.substring(0, seatsIdsByFieldId.length() - 1);
        }
        return seatIds;
    }

    @Override
<<<<<<< HEAD
    public com.stylefeng.guns.service.order.vo.MoocOrderT getOrderById(String id) {
        com.stylefeng.guns.service.order.vo.MoocOrderT moocOrderTFromAPI = new com.stylefeng.guns.service.order.vo.MoocOrderT();
=======
    public MoocOrder getOrderById(String id) {
        MoocOrder moocOrderTFromAPI = new MoocOrder();
>>>>>>> e84424777f24af9ae68aed7ed9cc182b13983f23
        MoocOrderT moocOrderT = moocOrderTMapper.selectById(id);
        BeanUtils.copyProperties(moocOrderT, moocOrderTFromAPI);
        return moocOrderTFromAPI;
    }
}
