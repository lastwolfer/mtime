package com.stylefeng.guns.rest.promo.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.constant.StockLogStatus;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.exception.GunsExceptionEnum;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoOrderMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoStockMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeStockLogMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromo;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromoOrder;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromoStock;
import com.stylefeng.guns.rest.common.persistence.model.MtimeStockLog;
import com.stylefeng.guns.rest.promo.rocketmq.MqProducer;
import com.stylefeng.guns.service.cinema.CinemaService;
import com.stylefeng.guns.service.cinema.vo.CinemaInfoVo;
import com.stylefeng.guns.service.cinema.vo.RespVo;
import com.stylefeng.guns.service.promo.PromoService;
import com.stylefeng.guns.service.promo.vo.PromoConditionVo;
import com.stylefeng.guns.service.promo.vo.PromoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.stylefeng.guns.consistant.RedisPrefixConsistant.*;

@Component
@Service
@Slf4j
public class PromoServiceImpl implements PromoService {
    @Autowired
    MtimePromoMapper mtimePromoMapper;
    @Autowired
    MtimePromoStockMapper mtimePromoStockMapper;
    @Reference(interfaceClass = CinemaService.class,check=false)
    CinemaService cinemaService;

    /**根据秒杀条件取出秒杀活动列表。当不传入的时候参数均为99，查询所有的秒杀活动列表
     * @param promoConditionVo
     * @return
     */
    @Override
    public RespVo getPromo(PromoConditionVo promoConditionVo) {
        //目前暂时只能显示全部
        List<PromoVo> list = new ArrayList<>();
        EntityWrapper<MtimePromo> promoEntityWrapper = new EntityWrapper<>();
        List<MtimePromo> mtimePromos = mtimePromoMapper.selectList(promoEntityWrapper);
        for (MtimePromo mtimePromo : mtimePromos) {
            PromoVo promoVo = new PromoVo();
            //影院ID
            Integer cinemaId = mtimePromo.getCinemaId();
            //根据cinema_id查找影院
            CinemaInfoVo cinema = cinemaService.getCinemaById(cinemaId);
            //影院信息赋给返回bean
            BeanUtils.copyProperties(cinema,promoVo);
            //其他信息
            promoVo.setPrice(mtimePromo.getPrice());
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            String startTime = dateFormat.format(mtimePromo.getStartTime());
            String endTime = dateFormat.format(mtimePromo.getEndTime());
            promoVo.setStartTime(startTime);
            promoVo.setEndTime(endTime);
            promoVo.setStatus(mtimePromo.getStatus());
            promoVo.setDescription(mtimePromo.getDescription());
            promoVo.setUuid(mtimePromo.getUuid());
            //库存信息
            EntityWrapper<MtimePromoStock> stockEntityWrapper = new EntityWrapper<>();
            stockEntityWrapper.eq("promo_id",mtimePromo.getUuid());
            List<MtimePromoStock> mtimePromoStocks = mtimePromoStockMapper.selectList(stockEntityWrapper);
            for (MtimePromoStock mtimePromoStock : mtimePromoStocks) {
                Integer stock = mtimePromoStock.getStock();
                promoVo.setStock(stock);
            }
            list.add(promoVo);
        }
        if(list.size() == 0){
            RespVo respVo = new RespVo();
            respVo.setStatus(1);
            respVo.setMsg("错误");
            return  respVo;
        }
        RespVo respVo = new RespVo();
        respVo.setData(list);
        respVo.setStatus(0);
        return  respVo;
    }

    /**执行本地事务
     * 第一步：插入订单数据
     * 第二步：减少redis库存
     * @param promoId
     * @param amount
     * @return
     */
    @Autowired
    MtimePromoOrderMapper mtimePromoOrderMapper;
    @Autowired
    RedisTemplate redisTemplate;

    private ExecutorService executorService;
    @PostConstruct
    public void init(){
        //创建一个线程池，里面有十个线程
        executorService = Executors.newFixedThreadPool(10);
    }

    @Transactional(propagation = Propagation.REQUIRED)//本地事务
    @Override
    public PromoVo createOrder(Integer promoId, Integer amount,Integer userId,String stockLogId) {
        //第一步：参数校验
        if(promoId == null || amount == null || userId ==null){
            return null;
        }
        //第二步：生成秒杀订单
        MtimePromoOrder mtimePromoOrder = savePromoOrder(promoId, amount, userId);
        if(mtimePromoOrder == null){
            //假如本地事务执行失败，更新库存流水记录的状态--失败
            //使用线程池，用异步更改，不然也会被回滚
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    mtimeStockLogMapper.updateStatusById(stockLogId, StockLogStatus.FAIL.getIndex());
                }
            });
            //抛出异常，事务回滚
            throw new GunsException(GunsExceptionEnum.FILE_NOT_FOUND);
        }
        //第三步：减少redis缓存中的库存
        publishPromoStock();
        Boolean result = decreaseRedisStock(promoId,amount);
        //如果返回的是false，就返回null
        if(!result){
            //假如本地事务执行失败，更新库存流水记录的状态--失败
            /*executorService.submit(new Runnable() {
                @Override
                public void run() {
                    mtimeStockLogMapper.updateStatusById(stockLogId, StockLogStatus.FAIL.getIndex());
                }
            });*/
            //或者使用lamuda表达式
            executorService.submit(() ->{
                mtimeStockLogMapper.updateStatusById(stockLogId, StockLogStatus.FAIL.getIndex());
            });
            //抛出异常，事务回滚
            throw new GunsException(GunsExceptionEnum.FILE_NOT_FOUND);
        }
        //假如本地事务执行成功，更新库存流水记录的状态--成功
        mtimeStockLogMapper.updateStatusById(stockLogId, StockLogStatus.SUCCESS.getIndex());
        //返回前端
        PromoVo promoVo = new PromoVo();
        BeanUtils.copyProperties(mtimePromoOrder,promoVo);
        return promoVo;
    }

    /**扣减redis缓存中的库存数
     * @param promoId 通过promoId为key
     * @param amount 要减去的数量
     * @return true为扣减成功 false为扣减失败
     */
    private Boolean decreaseRedisStock(Integer promoId, Integer amount) {
        //生成一个包含promoId的String类型的key
        Integer prefix1 = (Integer) redisTemplate.opsForValue().get("PROMO_STOCK_CACHE__PREFIX_1");
        System.out.println(prefix1);
        String key = PROMO_STOCK_CACHE_PREFIX + promoId;
        Long increment = redisTemplate.opsForValue().increment(key, amount * -1);
        //如果库存不足，需要回补回来
        if(increment < 0){
            redisTemplate.opsForValue().increment(key,amount);
            return false;
        }
        //如果库存等于0，则打上库存已告罄的标记
        if(increment == 0){
            String soldedKey = PROMO_STOCK_SOLDED_PREFIX + promoId;
            redisTemplate.opsForValue().set(soldedKey,"solded");
            redisTemplate.expire(soldedKey,2,TimeUnit.HOURS);
        }
       return true;
    }

    //生成秒杀订单
    private MtimePromoOrder savePromoOrder(Integer promoId, Integer amount, Integer userId) {
        MtimePromoOrder promoOrder = new MtimePromoOrder();
        //组装promoOrder的参数
        //生成秒杀订单id
        String uuid = UUID.randomUUID().toString();
        promoOrder.setUuid(uuid);
        promoOrder.setUserId(userId);
        //获得该秒杀活动信息
        MtimePromo mtimePromo = mtimePromoMapper.selectById(promoId);
        Integer cinemaId = mtimePromo.getCinemaId();
        promoOrder.setCinemaId(cinemaId);
        BigDecimal promoPrice = mtimePromo.getPrice();//活动单价
        int promoPrice1 = promoPrice.intValue();
        promoOrder.setPrice(BigDecimal.valueOf(promoPrice1*amount));
        promoOrder.setAmount(amount);
        //时间信息
        promoOrder.setStartTime(mtimePromo.getStartTime());
        promoOrder.setEndTime(mtimePromo.getEndTime());
        promoOrder.setCreateTime(new Date());
        //exchangeCode暂时设置不了 先用uuid代替一下吧
        promoOrder.setExchangeCode(uuid);
        //存入数据库
        Integer insert = mtimePromoOrderMapper.insert(promoOrder);
        if(insert != 1){
            return null;
        }
        return promoOrder;
    }

    /**
     * 初始化流水库存状态
     *
     * @param promoId
     * @param amount
     * @return 该条记录主键
     */
    @Autowired
    private MtimeStockLogMapper mtimeStockLogMapper;
    @Override
    public String initPromoStockLog(Integer promoId, Integer amount) {
        MtimeStockLog mtimeStockLog = new MtimeStockLog();
        mtimeStockLog.setAmount(amount);
        mtimeStockLog.setPromoId(promoId);
        String uuid = UUID.randomUUID().toString();
        mtimeStockLog.setUuid(uuid);
        mtimeStockLog.setStatus(StockLogStatus.INIT.getIndex());
        Integer insert = mtimeStockLogMapper.insert(mtimeStockLog);
        if(insert == 1){
            return uuid;
        }
        return null;
    }


    /**将库存信息发布到缓存
     * 需要注意的是，如果redis中已经有了信息，就不需要再发布了
     * 而且，不同的promoId要进行不同的判断
     * @return
     */
    //秒杀令牌对于库存的倍数
    public static  final Integer PROMO_TOKEN_TIMES = 5;
    @Override
    public Boolean publishPromoStock() {
        //取出所有的秒杀活动库存表
        EntityWrapper<MtimePromoStock> stockEntityWrapper = new EntityWrapper<>();
        List<MtimePromoStock> mtimePromoStocks = mtimePromoStockMapper.selectList(stockEntityWrapper);
        for (MtimePromoStock mtimePromoStock : mtimePromoStocks) {
            Integer promoId = mtimePromoStock.getPromoId();
            String key = PROMO_STOCK_CACHE_PREFIX + promoId;
            //如果为空，就创建一个，而且设置永不过期
            if(redisTemplate.opsForValue().get(key) == null){
                redisTemplate.opsForValue().set(key,mtimePromoStock.getStock());
                //除了设置redis库存，还要根据库存定制化令牌的数目(秒杀大闸)
                String amountToken = PROMO_STOCK_AMOUNT_LIMIT + promoId;
                Integer amount = PROMO_TOKEN_TIMES;
                redisTemplate.opsForValue().set(amountToken,amount*mtimePromoStock.getStock());
            }
        }
        return  true;
    }


    /**
     * 暴露向注册中心发送事务型消息的接口
     * 只是一个工具方法，包装了真正执行干活的方法
     * @param promoId
     * @param amount
     * @param userId
     * @return
     */
    @Autowired
    MqProducer mqProducer;

    @Override
    public Boolean savePromoOrderInTransaction(Integer promoId, Integer amount, Integer userId,String stockLogId) {
        Boolean aBoolean = mqProducer.sendStockMessageIntransaction(promoId, amount, userId,stockLogId);
        return aBoolean;
    }

    /**
     * 生成秒杀令牌
     * @param promoId 活动id
     * @return token
     */
    @Override
    public String generateToken(Integer promoId,Integer userId) {
        //先要判断一下秒杀令牌的数目是否足够
        String amountToken = PROMO_STOCK_AMOUNT_LIMIT + promoId;
        Long amount = redisTemplate.opsForValue().increment(amountToken,-1);
        if(amount < 0 ){
            return null;
        }
        //随机生成一个token
        String token = UUID.randomUUID().toString();
        //把该token放在redis中，方便gateway判断是否有效
        String key = String.format(USER_PROMO_TOKEN_PREFIX_,promoId,userId);
        redisTemplate.opsForValue().set(key,token);
        return token;
    }

}
