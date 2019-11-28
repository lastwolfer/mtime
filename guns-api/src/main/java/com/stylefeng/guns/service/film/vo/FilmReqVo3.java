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
    private Integer showType;
    private Integer sortId;
    private Integer catId;
    private Integer sourceId;
    private Integer yearId;
    private Integer nowPage;
    private Integer pageSize;
}
