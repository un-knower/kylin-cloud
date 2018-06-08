package com.kylin.user.service;

import com.baomidou.mybatisplus.service.IService;
import com.kylin.user.entity.UserToRole;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liugh123
 * @since 2018-05-03
 */
public interface IUserToRoleService extends IService<UserToRole> {

    /**
     * 根据用户ID查询人员角色
     * @param userId 用户ID
     * @return  结果
     */
    UserToRole selectByUserId(Integer userId);

}
