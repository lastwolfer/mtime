package com.stylefeng.guns.rest.seckill.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.exception.GunsExceptionEnum;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoOrderMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoStockMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeStockLogMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromo;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromoOrder;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromoStock;
import com.stylefeng.guns.rest.common.persistence.model.MtimeStockLog;
import com.stylefeng.guns.rest.exception.GunsException;
import com.stylefeng.guns.rest.mq.MqConsumer;
import com.stylefeng.guns.rest.mq.MqProducer;
import com.stylefeng.guns.rest.seckill.constant.StockLogStatus;
import com.stylefeng.guns.service.cinema.CinemaService;
import com.stylefeng.guns.service.cinema.vo.CinemasDataVo;
import com.stylefeng.guns.service.cinema.vo.CinemasReqVo;
import com.stylefeng.guns.service.cinema.vo.RespVo;
import com.stylefeng.guns.service.film.vo.BaseRespVo;
import com.stylefeng.guns.service.seckill.SeckillService;
import com.stylefeng.guns.service.seckill.prefix.RedisPrefixConsistant;
import com.stylefeng.guns.service.seckill.vo.CreateOrderVo;
import com.stylefeng.guns.service.seckill.vo.PromoOrderVO;
import com.stylefeng.guns.service.seckill.vo.PublishPromoStockVo;
import com.stylefeng.guns.service.seckill.vo.SeckillVo;
import com.stylefeng.guns.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
@Slf4j
@Component
@Service(interfaceClass = SeckillService.class)
public class SeckillServiceImpl implements SeckillService {

    private static final Integer PROMO_TOKEN_TIMES =10 ;
    @Autowired
    private MtimePromoMapper promoMapper;

    @Autowired
    private MtimeStockLogMapper stockLogMapper;

    @Autowired
    private MqProducer mqProducer;

    @Autowired
    MtimePromoStockMapper promoStockMapper;

    @Autowired
    RedisTemplate redisTemplate;



    @Autowired
    private MtimePromoStockMapper stockMapper;

    private ExecutorService executorService;
    @PostConstruct
    public void init(){
        //创建一个线程池，里面有10个线程
        executorService= Executors.newFixedThreadPool(10);
    }

    //设置兑换码有效期为3个月
    private static final Integer EXCHANGE_CODE_VALID_MONTH = 3;

    //库存缓存前缀
    private static final String PROMO_STOCK_CACHE_PREFIX = "promoId";



    @Override
    public PublishPromoStockVo publishPromoStock() {
        //进入mtime_promo_stock查出所有库存信息
        EntityWrapper<MtimePromoStock> wrapper = new EntityWrapper<>();
        List<MtimePromoStock> stockList = promoStockMapper.selectList(wrapper);
        if(stockList!=null){
            for (MtimePromoStock promoStock : stockList) {
                Integer o = (Integer) redisTemplate.opsForValue().get("promoId"+promoStock.getPromoId());
                if(o==null){
                    redisTemplate.opsForValue().set("promoId"+promoStock.getPromoId(),promoStock.getStock());
                    redisTemplate.expire("promoId"+promoStock.getPromoId(),60*24*7, TimeUnit.SECONDS);

                    //存入秒杀令牌的数量在redis里面
                    String amountKey  = ""+RedisPrefixConsistant.PROMO_STOCK_AMOUNT_LIMIT +  promoStock.getPromoId();
                    Integer amountValue = promoStock.getStock() * PROMO_TOKEN_TIMES;
                    redisTemplate.opsForValue().set(amountKey,amountValue);
                    redisTemplate.expire(amountKey,60*24*7, TimeUnit.SECONDS);
                }


            }
        }






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
                    if(seckillVo.getStock()>0){
                        list.add(seckillVo);
                    }
                }
            }
        }
        BaseRespVo ok = BaseRespVo.ok(list);
        return ok;
    }

    /**
     * 第一步 校验参数
     *
     * 第二步 生产订单插入数据库
     *
     * 第三步 扣减redis中的库存
     *
     * 第四步 发送异步消息 扣减数据库库存
     */
    @Autowired
    MtimePromoOrderMapper promoOrderMapper;
    @Autowired
    MqProducer producer;
    @Autowired
    MqConsumer consumer;
    public CreateOrderVo createOrder(Integer promoId, Integer amount, String promoToken, Integer uuid){
        //校验参数，看请求是否大于库存
        //进radis去库存
        Integer stock = (Integer) redisTemplate.opsForValue().get("promoId" + promoId);
        if(amount>stock){
            return CreateOrderVo.fail("超出库存，下单失败！！！");
        }
        //生产订单插入数据库
        //1.获取用户信息

        //UserInfo userInfo = (UserInfo) redisTemplate.opsForValue().get(promoToken);
        Integer userId = uuid;
        //2.通过promoId获取整个优惠活动
        MtimePromo mtimePromo = promoMapper.selectById(promoId);
        MtimePromoOrder mtimePromoOrder = new MtimePromoOrder();
        mtimePromoOrder.setUserId(userId);
        mtimePromoOrder.setCinemaId(mtimePromo.getCinemaId());
        mtimePromoOrder.setExchangeCode("");
        mtimePromoOrder.setAmount(amount);
        double p = amount.doubleValue() * mtimePromo.getPrice().doubleValue() * 1.0;
        mtimePromoOrder.setPrice(new BigDecimal(p));
        mtimePromoOrder.setStartTime(mtimePromo.getStartTime());
        mtimePromoOrder.setCreateTime(new Date());
        mtimePromoOrder.setEndTime(mtimePromo.getEndTime());
        //3.存入数据库
        promoOrderMapper.insertOrder(mtimePromoOrder);


        //4.扣减redis库存
        redisTemplate.opsForValue().set("promoId" + promoId,stock-amount);


        /*//第四步 发送异步消息 扣减数据库库存
        Boolean aBoolean = producer.decreaseStock(promoId, stock - amount);
        System.out.println(aBoolean);
*/
        return CreateOrderVo.ok();
    }

    @Override
    public String initPromoStockLog(Integer promoId, Integer amount) {
        MtimeStockLog mtimeStockLog = new MtimeStockLog();
        mtimeStockLog.setUuid(UUID.randomUUID()+"");
        mtimeStockLog.setPromoId(promoId);
        mtimeStockLog.setAmount(amount);
        mtimeStockLog.setStatus(StockLogStatus.INIT.getIndex());

        //将这条记录插入记录表中
        Integer insert = stockLogMapper.insert(mtimeStockLog);
        if(insert>0){
            return mtimeStockLog.getUuid();
        }
        return null;
    }

    @Override
    public Boolean savePromoOrderInTransaction(Integer promoId, Integer userId, Integer amount, String stockLogId) {
        Boolean res = mqProducer.sendStockMessageIntransaction(promoId, amount, userId, stockLogId);
        return res;
    }

    @Override
    public PromoOrderVO savePromoOrderVO(Integer promoId, Integer userId, Integer amount, String stockLogId) {
        //参数校验
        processParam(promoId, userId, amount);

        MtimePromo promo = promoMapper.selectById(promoId);

        //订单入库
        MtimePromoOrder promoOrder = savePromoOrder(promo, userId, amount);


        if (promoOrder == null) {
            //更新库存流水的状态   ----失败  第一个是发送消息  第二个是异步线程

//            executorService.submit(new Runnable() {
//                @Override
//                public void run() {
//                    stockLogMapper.updateStatusById(stockLogId,StockLogStatus.FAIL.getIndex());
//                }
//            });
            //通过消息去异步化
            executorService.submit(() -> {
                stockLogMapper.updateStatusById(stockLogId, StockLogStatus.FAIL.getIndex());
            });

            throw new GunsException(GunsExceptionEnum.INVLIDE_DATE_STRING);
        }


        //扣减库存
        Boolean ret = decreaseStock(promoId, amount);

        if (!ret) {

            //更新库存流水的状态   ----失败
            executorService.submit(() -> {
                stockLogMapper.updateStatusById(stockLogId, StockLogStatus.FAIL.getIndex());
            });

            throw new GunsException(GunsExceptionEnum.DATABASE_ERROR);
        }


        //组装参数返回前端
        PromoOrderVO promoOrderVO = buildPromoOrderVO(promoOrder);

        //假如本地事务执行成功  更新库存流水记录的状态 -----成功
        stockLogMapper.updateStatusById(stockLogId, StockLogStatus.SUCCESS.getIndex());

        //返回前端
        return promoOrderVO;
    }

    @Override
    public String generateToken(Integer promoId, Integer userId) {
        String killTokenKey=String.format(RedisPrefixConsistant.USER_PROMO_TOKEN_PREFIX,promoId,userId);

        //判断秒杀令牌存量是否存在
        String amountKey  = ""+RedisPrefixConsistant.PROMO_STOCK_AMOUNT_LIMIT +  promoId;
        Long increment = redisTemplate.opsForValue().increment(amountKey, -1);
        if(increment<0){
            return null;
        }

        UUID uuid = UUID.randomUUID();
        redisTemplate.opsForValue().set(killTokenKey,uuid);
        return uuid+"";
    }

    private PromoOrderVO buildPromoOrderVO(MtimePromoOrder promoOrder) {

        PromoOrderVO orderVO = new PromoOrderVO();
        orderVO.setUuid(promoOrder.getUuid());
        orderVO.setUserId(promoOrder.getUserId());
        orderVO.setAffectedEndTime(promoOrder.getEndTime());
        orderVO.setAffectedStartTime(promoOrder.getStartTime());
        orderVO.setAmount(promoOrder.getAmount());
        orderVO.setCinemaId(promoOrder.getCinemaId());
        orderVO.setCreateTime(promoOrder.getCreateTime());
        orderVO.setExchangeCode(promoOrder.getExchangeCode());
        orderVO.setPrice(promoOrder.getPrice().doubleValue());
        return orderVO;
    }

    private Boolean decreaseStock(Integer promoId, Integer amount) {
        String key = PROMO_STOCK_CACHE_PREFIX + promoId;

        Long increment = redisTemplate.opsForValue().increment(key, amount * -1);

        /**不要这么做
         *
         * 1 取
         *
         * 2 设置
         *
         * 3 存
         */

        //如果库存不足
        if (increment < 0) {
            log.info("库存已售完,promoId:{}", promoId);
            redisTemplate.opsForValue().increment(key, amount);
            return false;
        }

//        // 发送异步消息扣减数据库库存
//        Boolean result = mqProducer.asyncDecreaseStock(promoId,amount);
//
//        return result;

        return true;

    }

    private MtimePromoOrder savePromoOrder(MtimePromo promo, Integer userId, Integer amount) {
        //组装promoOrder
        MtimePromoOrder promoOrder = buidPromoOrder(promo, userId, amount);
        //存入数据库
        Integer insertRet = promoOrderMapper.insert(promoOrder);
        if (insertRet < 1) {
            log.info("生成秒杀订单失败！promoOrder:{}", JSON.toJSONString(promoOrder));
            return null;
        }
        return promoOrder;
    }

    private MtimePromoOrder buidPromoOrder(MtimePromo promo, Integer userId, Integer amount) {

        MtimePromoOrder promoOrder = new MtimePromoOrder();
        String uuid = UUID.randomUUID()+"";
        Integer cinemaId = promo.getCinemaId();
        String exchangeCode =  UUID.randomUUID()+"";
        //兑换开始时间和兑换结束时间 为从现在开始，到未来三个月之内
        Date startTime = new Date();
        Date endTime = DateUtil.getAfterMonthDate(EXCHANGE_CODE_VALID_MONTH);
        BigDecimal amountDecimal = new BigDecimal(amount);
        BigDecimal price = amountDecimal.multiply(promo.getPrice()).setScale(2, RoundingMode.HALF_UP);
        promoOrder.setUuid(uuid);
        promoOrder.setUserId(userId);
        promoOrder.setCinemaId(cinemaId);
        promoOrder.setExchangeCode(exchangeCode);
        promoOrder.setStartTime(startTime);
        promoOrder.setEndTime(endTime);
        promoOrder.setAmount(amount);
        promoOrder.setPrice(price);
        promoOrder.setCreateTime(new Date());
        return promoOrder;
    }

    private void processParam(Integer promoId, Integer userId, Integer amount) {

        if (promoId == null) {
            log.info("promoId不能为空！");
            throw new GunsException(GunsExceptionEnum.REQUEST_NULL);
        }
        if (userId == null) {
            log.info("userId不能为空！");
            throw new GunsException(GunsExceptionEnum.REQUEST_NULL);
        }
        if (amount == null) {
            log.info("amount不能为空！");
            throw new GunsException(GunsExceptionEnum.REQUEST_NULL);
        }
    }


}
