package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 23:04
 */

@Data
public class RoleVo implements Serializable {
    private static final long serialVersionUID = 1241245625960368776L;
    private Integer uuid;
    private String actorId;
    private String roleName;
}
