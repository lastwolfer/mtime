package com.stylefeng.guns.service.film.vo.response;

import lombok.Data;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/29
 * @time 15:06
 */

/**
 * 这个类是 方法 影片查询接口 返回的响应报文中Json格式
 */
@Data
public class BaseVoDetail extends BaseVo {
    private static final long serialVersionUID = 8537162377934993049L;
    private String imgPre;
    private Integer nowPage;
    private Integer totalPage;
    private Object Date;
}
