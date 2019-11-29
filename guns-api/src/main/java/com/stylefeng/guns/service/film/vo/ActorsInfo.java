package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 21:54
 */

@Data
public class ActorsInfo implements Serializable {
    private static final long serialVersionUID = 3418994123358541322L;
    private Director director;
    private List<Actor> actors;
}
