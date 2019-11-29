package com.stylefeng.guns.service.cinema;

import com.stylefeng.guns.service.cinema.vo.CinemaCondition;
import com.stylefeng.guns.service.cinema.vo.RespVo;

public interface ConditionService {
     RespVo getCondition(CinemaCondition cinemaCondition);
}
