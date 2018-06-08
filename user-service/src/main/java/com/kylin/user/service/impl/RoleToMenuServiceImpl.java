package com.kylin.user.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.kylin.user.entity.RoleToMenu;
import com.kylin.user.mapper.RoleToMenuMapper;
import com.kylin.user.service.IRoleToMenuService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liugh123
 * @since 2018-05-03
 */
@Service
public class RoleToMenuServiceImpl extends ServiceImpl<RoleToMenuMapper, RoleToMenu> implements IRoleToMenuService {

    @Override
    @Cacheable(value = "RoleToMenu",keyGenerator="wiselyKeyGenerator")
    public List<RoleToMenu> selectByRoleId(Integer roleId) {
        EntityWrapper<RoleToMenu> ew = new EntityWrapper<>();
        ew.where("role_id={0}", roleId);
        return this.selectList(ew);
    }
}
