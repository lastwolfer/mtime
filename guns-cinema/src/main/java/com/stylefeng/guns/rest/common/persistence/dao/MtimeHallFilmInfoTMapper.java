package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeHallFilmInfoT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 影厅电影信息表 Mapper 接口
 * </p>
 *
 * @author pandax
 * @since 2019-11-29
 */
public interface MtimeHallFilmInfoTMapper extends BaseMapper<MtimeHallFilmInfoT> {

    @Select("select * from mtime_hall_film_info_t where film_id = #{key}")
    MtimeHallFilmInfoT selectByFilmId(Integer key);
}
