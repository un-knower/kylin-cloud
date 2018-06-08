package com.kylin.user.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.kylin.user.entity.UserToRole;
import com.kylin.user.mapper.UserToRoleMapper;
import com.kylin.user.service.IUserToRoleService;
import com.kylin.core.util.ComUtil;
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
public class UserToRoleServiceImpl extends ServiceImpl<UserToRoleMapper, UserToRole> implements IUserToRoleService {

    @Override
    public UserToRole selectByUserId(Integer userId) {
        EntityWrapper<UserToRole> ew = new EntityWrapper<>();
        ew.where("user_id={0}", userId);
        List<UserToRole> userToRoleList = this.selectList(ew);
        return ComUtil.isEmpty(userToRoleList)? null: userToRoleList.get(0);
    }
}
