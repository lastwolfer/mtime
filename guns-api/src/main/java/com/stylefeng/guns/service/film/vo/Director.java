package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 21:55
 */

@Data
public class Director implements Serializable {
    private static final long serialVersionUID = -379408656529111487L;
    private String imgAddress;
    private String directorName;
}
