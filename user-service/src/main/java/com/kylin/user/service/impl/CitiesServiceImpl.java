package com.kylin.user.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.kylin.user.entity.Cities;
import com.kylin.user.mapper.CitiesMapper;
import com.kylin.user.service.ICitiesService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liugh123
 * @since 2018-05-11
 */
@Service
public class CitiesServiceImpl extends ServiceImpl<CitiesMapper, Cities> implements ICitiesService {

}
