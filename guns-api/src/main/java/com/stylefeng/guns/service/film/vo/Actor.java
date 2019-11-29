package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 21:56
 */

@Data
public class Actor implements Serializable {
    private static final long serialVersionUID = 2694042268013103109L;
    private String imgAddress;
    private String directorName;
    private String roleName;
}
