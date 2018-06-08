package com.kylin.user.service;

import com.kylin.user.entity.User;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liugh123
 * @since 2018-06-08
 */
public interface IUserService extends IService<User> {

     User getUserById(Long id);

    void addAccount(User user, String enableUrl);

     boolean enable(String key);

    User auth(String email, String passwd);

    User getLoginedUserByToken(String token);

    void invalidate(String token);
}
