package com.stylefeng.guns.service.film.vo;

import lombok.Data;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 17:49
 */

@Data
public class FilmReqVo3 {
    private Integer showType = 1;
    private Integer sortId = 1;
    private Integer catId = 99;
    private Integer sourceId = 99;
    private Integer yearId = 99;
    private Integer nowPage = 1;
    private Integer pageSize = 18;
}
