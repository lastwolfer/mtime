package com.stylefeng.guns.service.film.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Da
 * @version 1.0
 * @date 2019/11/28
 * @time 21:54
 */

@Data
public class ActorsInfo {
    private Director director;
    private List<Actor> actors;
}
