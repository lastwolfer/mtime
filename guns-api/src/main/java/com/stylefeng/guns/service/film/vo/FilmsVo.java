package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;


/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 17:40
 */

@Data
public class FilmsVo implements Serializable {
    private static final long serialVersionUID = -1733225035749131487L;
    //private String imgPre;
    private Integer nowPage;
    private Integer totalPage;
    private Object data;
    private String msg;
    private Integer status;
}
