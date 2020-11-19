package com.stylefeng.guns.consistant;

/**
 * 缓存Key前缀
 * 写在api中，方便模块和gateway都能调用
 */
public class RedisPrefixConsistant {

    //缓存前缀
    public  static final String PROMO_STOCK_CACHE_PREFIX = "PROMO_STOCK_CACHE_PREFIX_";

    //redis库存告罄前缀
    public static final String PROMO_STOCK_SOLDED_PREFIX = "PROMO_STOCK_SOLDED_PREFIX_";

    //秒杀令牌数量限制前缀
    public static  final String PROMO_STOCK_AMOUNT_LIMIT = "PROMO_STOCK_AMOUNT_LIMIT_";

    //秒杀令牌存在前缀
    public static  final String USER_PROMO_TOKEN_PREFIX_ = "USER_PROMO_TOKEN_PREFIX_%s_USERID_%s";

}
