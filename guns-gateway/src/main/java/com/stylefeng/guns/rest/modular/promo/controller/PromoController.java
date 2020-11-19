package com.stylefeng.guns.rest.modular.promo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.util.concurrent.RateLimiter;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.exception.GunsExceptionEnum;
import com.stylefeng.guns.service.cinema.vo.RespVo;
import com.stylefeng.guns.service.promo.PromoService;
import com.stylefeng.guns.service.promo.vo.PromoConditionVo;
import com.stylefeng.guns.service.user.beans.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.stylefeng.guns.consistant.RedisPrefixConsistant.USER_PROMO_TOKEN_PREFIX_;

@RestController
@RequestMapping("promo")
public class PromoController {
    @Autowired
    RedisTemplate redisTemplate;

    @Reference(interfaceClass = PromoService.class,check = false)
    PromoService promoService;

    /**根据影院状态查询秒杀订单列表
     * 当不传入的时候默认为99，查询所有
     * @param promoConditionVo
     * @return
     */
    @RequestMapping("getPromo")
    public RespVo gerPromo(PromoConditionVo promoConditionVo){
        RespVo respVo = promoService.getPromo(promoConditionVo);
        return respVo;
    }

    /**
     * 秒杀订单下单接口
     * 需要先登陆
     * @param
     * @param promoId 秒杀活动id
     * @param amount 下单购买数量
     * @return
     */
    //设置线程池大小，队列泄洪，保护下单接口
    private ExecutorService executorService;
    //设置令牌桶，动态的给予令牌
    private RateLimiter rateLimiter;
    @PostConstruct
    public void init(){
        //有四种构造线程池的方法
        //线程池大小就限定了多少人能执行下单的方法，其余的请求只能等待
        //100即为拥塞窗口
        executorService = Executors.newFixedThreadPool(500);
       /* executorService = Executors.newSingleThreadExecutor();
        executorService = Executors.newCachedThreadPool();
        executorService = Executors.newScheduledThreadPool(100);*/
        //每秒产生100个令牌
        rateLimiter = RateLimiter.create(100);
    }
    @RequestMapping(value = "createOrder",method = RequestMethod.POST)
    public RespVo createOrder(@RequestParam(required = true,name = "promoId") Integer promoId,
                              @RequestParam(required = true,name = "amount") Integer amount,
                              @RequestParam(required = true,name = "promoToken") String promoToken,
                              HttpServletRequest request,
                              HttpServletResponse response){
        //获取一个令牌
        double acquire = rateLimiter.acquire();
        if(acquire < 0){
            return RespVo.fail(999,"获取令牌失败");
        }
        //获得请求头中携带的的token
        String authorization = request.getHeader("Authorization");
        String authToken = authorization.substring(7);
        UserInfo user = (UserInfo) redisTemplate.opsForValue().get(authToken);
        //校验有无token,是否登录过
        if(user == null){
            return RespVo.fail(999,"获取用户失败，请重新登录");
        }
        Integer userId = user.getUuid();
        //对前端传来的参数进行校验
        if(promoId == null || amount == null || amount < 0 || amount > 5 ){
            return RespVo.fail(999,"参数错误");
        }
        //判断秒杀令牌是否合法
        String Tokenkey = String.format(USER_PROMO_TOKEN_PREFIX_,promoId,userId);
        if(!redisTemplate.hasKey(Tokenkey)){
            return RespVo.fail(999,"秒杀令牌不合法");
        }
        String tokenInredis = (String) redisTemplate.opsForValue().get(Tokenkey);
        if(!promoToken.equals(tokenInredis)){
            return RespVo.fail(999,"秒杀令牌不合法");
        }
        //队列泄洪，保护我方核心下单接口
        Future<Boolean> future = executorService.submit(() -> {
            Boolean ret = null;
            try {
                //下单之前，初始化一条库存流水，并把状态设置为初始值，并返回一个这条记录的主键id
                String stockLogId = promoService.initPromoStockLog(promoId, amount);
                if (StringUtils.isBlank(stockLogId)) {
                    throw new GunsException(GunsExceptionEnum.FILE_NOT_FOUND);
                }
                //下单接口：创建订单 扣减redis和DB库存
                ret = promoService.savePromoOrderInTransaction(promoId, amount, userId, stockLogId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!ret) {
                throw new GunsException(GunsExceptionEnum.FILE_NOT_FOUND);
            }
            return ret;
        });
        Boolean aBoolean =false;
        try {
           aBoolean = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return RespVo.fail(999,"参数错误");
        } catch (ExecutionException e) {
            e.printStackTrace();
            return RespVo.fail(999,"参数错误");
        } catch (GunsException e) {
            e.printStackTrace();
            return RespVo.fail(999,"参数错误");
        }
        if(!aBoolean){
            return RespVo.fail(999,"参数错误");
        }
        return RespVo.ok("下单成功");
    }

    /**
     * 获取秒杀令牌接口(下单资格凭证)
     * 需要先登陆
     * @param promoId 秒杀活动id
     * @return
     */
    @RequestMapping(value = "generateToken",method = RequestMethod.GET)
    public RespVo generateToken(@RequestParam("promoId") Integer promoId,
                                HttpServletRequest request,
                                HttpServletResponse response){
        //取出用户id
        String authorization = request.getHeader("Authorization");
        String authToken = authorization.substring(7);
        UserInfo user = (UserInfo) redisTemplate.opsForValue().get(authToken);
        if(user == null){
            return RespVo.fail(999,"获取用户失败，请重新登录");
        }
        Integer userId = user.getUuid();
        //前置库存告罄判断
        //判断一下redis库存中有没有库存，没有的话直接返回
        Boolean solded = redisTemplate.hasKey("PROMO_STOCK_SOLDED_PREFIX_");
        if(solded){
            return RespVo.fail(999,"库存已经告罄");
        }
        //先要根据promoId判断 1.活动是否真实存在 2.判断活动是否有效 3.判断活动是否正在进行
        String token = null;
        try {
            token = promoService.generateToken(promoId,userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GunsException(GunsExceptionEnum.FILE_NOT_FOUND);
        }
        if(token == null){
            return new RespVo(1,"获取失败");
        }
        return new RespVo(0,token);
    }

    /**将库存信息发布到缓存
     * @return
     */
    @RequestMapping("publishPromoStock")
    public RespVo publishPromoStock(){
        Boolean aBoolean = promoService.publishPromoStock();
        return RespVo.ok(aBoolean);
    }

}
