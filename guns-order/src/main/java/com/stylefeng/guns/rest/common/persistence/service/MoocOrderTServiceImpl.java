package com.stylefeng.guns.rest.common.persistence.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.service.order.MoocOrderTService;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 订单信息表 服务实现类
 * </p>
 *
 * @author Mainzer
 * @since 2019-11-30
 */
@Component
@Service(interfaceClass = MoocOrderTService.class)
public class MoocOrderTServiceImpl extends ServiceImpl<MoocOrderTMapper, MoocOrderT> implements MoocOrderTService {

}
