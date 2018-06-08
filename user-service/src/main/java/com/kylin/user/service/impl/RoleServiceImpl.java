package com.kylin.user.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.kylin.user.entity.Role;
import com.kylin.user.mapper.RoleMapper;
import com.kylin.user.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liugh123
 * @since 2018-05-03
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
