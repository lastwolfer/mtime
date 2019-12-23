package com.stylefeng.guns.rest.modular.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.rest.exception.GunsExceptionEnum;
import com.stylefeng.guns.service.film.vo.BaseRespVo;
import com.stylefeng.guns.service.seckill.SeckillService;
import com.stylefeng.guns.service.seckill.prefix.RedisPrefixConsistant;
import com.stylefeng.guns.service.seckill.vo.CreateOrderVo;
import com.stylefeng.guns.service.seckill.vo.PublishPromoStockVo;
import com.stylefeng.guns.service.seckill.vo.ResponseVO;
import com.stylefeng.guns.service.user.MtimeUserService;
import com.stylefeng.guns.service.user.beans.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
public class SeckillController {

    @Reference(interfaceClass = SeckillService.class,check = false)
    SeckillService seckillService;
    @Autowired
    RedisTemplate redisTemplate;



    @RequestMapping("promo/publishPromoStock")
    public PublishPromoStockVo publishPromoStock() {
        PublishPromoStockVo publishPromoStockVo = seckillService.publishPromoStock();
        return publishPromoStockVo;
    }
    @RequestMapping("promo/getPromo")
    public BaseRespVo getPromo(@RequestParam(required = false,name = "brandId") Integer brandId,
                               @RequestParam(required = false,name = "hallType") Integer hallType,
                               @RequestParam(required = false,name = "areaId") Integer areaId,
                               @RequestParam(required = false,name = "pageSize") Integer pageSize,
                               @RequestParam(required = false,name = "nowPage") Integer nowPage) {
        if(brandId==null){
            brandId=99;
            hallType=99;
            areaId=99;
            pageSize=12;
            nowPage=1;
        }
        BaseRespVo promo = seckillService.getPromo(brandId, hallType, areaId, pageSize, nowPage);
        return promo;
    }
    @Reference(interfaceClass = MtimeUserService.class,check = false)
    MtimeUserService userTService;

    //设置一个线程池
    private ExecutorService executorService ;

    private RateLimiter rateLimiter;


    @PostConstruct
    public void init(){
        executorService = Executors.newFixedThreadPool(100);

//        //初始化一个动态扩容无线程限制的线程池
//        executorService = Executors.newCachedThreadPool();
//
//        //创建一个只有一个线程的线程池
//        executorService = Executors.newSingleThreadExecutor();
//
//        //创建一个定时任务去执行的线程池
//        executorService = Executors.newScheduledThreadPool(10);

        //每秒产生10个令牌
        rateLimiter = RateLimiter.create(10);
    }
    @RequestMapping("promo/createOrder")
    public ResponseVO createOrder(@RequestParam(required = true, name = "promoId") Integer promoId,
                                     @RequestParam(required = true, name = "amount") Integer amount,
                                     @RequestParam(required = true,name = "promoToken") String promoToken,
                                     HttpServletRequest request, HttpServletResponse response){


        double acquire = rateLimiter.acquire();
        if (acquire < 0) {
            return ResponseVO.fail("秒杀失败！");
        }

        //获取用户信息
        UserInfo userInfo = getUserInfo();
        Integer userId=-1;
        if(userInfo!=null){
            userId=userInfo.getUuid();
        }


        //判定令牌是否合法
        //首先判定令牌是否存在
        String killTokenKey= String.format(RedisPrefixConsistant.USER_PROMO_TOKEN_PREFIX,promoId,userId);
        Boolean hasKey = redisTemplate.hasKey(killTokenKey);
        if(!hasKey){
            return ResponseVO.fail("秒杀令牌不合法！");
        }
        //判定秒杀令牌是否一致
        String tokenInRedis = (String) redisTemplate.opsForValue().get(killTokenKey);
        if(!StringUtils.equals(tokenInRedis,promoToken)){
            return ResponseVO.fail("秒杀令牌不合法！");
        }

        //参数校验
        //从redis中取出库存
        String promoStockKey="promoId"+promoId;
        Integer  promoStock= (Integer) redisTemplate.opsForValue().get(promoStockKey);
        if(amount>promoStock){
            return ResponseVO.fail("订单数量不合法！");
        }


        Boolean result = false;
        try {
            //下单之前 初始化一条库存流水 并把状态设置为初始值 并返回这条记录的主键id
            String stockLogId=seckillService.initPromoStockLog(promoId,amount);
            if (StringUtils.isBlank(stockLogId)) {
                log.info("下单失败！promoId:{},userId:{},amount:{}",promoId,userId,amount);
                throw new GunsException(GunsExceptionEnum.DATABASE_ERROR);
            }
            //下单接口
            //创建订单 扣减库存
            result=seckillService.savePromoOrderInTransaction(promoId,Integer.valueOf(userId),amount,stockLogId);
        } catch (Exception e) {
            log.info("下单失败！promoId:{},userId:{},amount:{}",promoId,userId,amount);
            throw new GunsException(GunsExceptionEnum.DATABASE_ERROR);
        }
        if (!result) {
            throw new GunsException(GunsExceptionEnum.DATABASE_ERROR);
        }
        return ResponseVO.ok();
    }


    /**
     * 获取请求头中的authorization的user信息
     * @return
     */
    public UserInfo getUserInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorization = request.getHeader("Authorization");
        String authToken = authorization.substring(7);
        return (UserInfo) redisTemplate.opsForValue().get(authToken);
    }
    /**
     *生成秒杀令牌
     * @return
     */
    @RequestMapping(value = "promo/generateToken",method = RequestMethod.GET)
    @ResponseBody
    public ResponseVO generateToken(@RequestParam(required = true, name = "promoId") Integer promoId, HttpServletRequest request, HttpServletResponse response) {

        //获取用户信息
        UserInfo userInfo = getUserInfo();
        Integer userId=-1;
        if(userInfo!=null){
            userId=userInfo.getUuid();
        }


//前置库存售罄的判断

        //先去判断库存是否已经售罄 如果库存已经售罄 则直接返回
        String soldedKey = RedisPrefixConsistant.PROMO_STOCK_NULL_PROMOID + promoId;
        //Boolean resl = redisTemplate.hasKey(soldedKey);
        Integer promoStock = (Integer) redisTemplate.opsForValue().get(soldedKey);
        if (promoId<=0) {
            return ResponseVO.fail("库存已经售罄");
        }


        if (userInfo == null) {
            log.info("获取用户失败！请用户重新登录！responseVO:{}", JSON.toJSONString(userInfo));
            return ResponseVO.expire();
        }

        //先要根据cinemaId判断
        // 1，判断活动是否真的存在
        // 2，判断活动是否有效
        // 3，判断活动是否正在进行

        String token = seckillService.generateToken(promoId,Integer.valueOf(userId));
        if (StringUtils.isBlank(token)) {
            return ResponseVO.fail("获取秒杀令牌失败");
        }
        return ResponseVO.ok(token);
    }

}
