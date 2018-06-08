package com.kylin.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.ImmutableMap;
import com.kylin.core.common.BeanHelper;
import com.kylin.core.util.HashUtils;
import com.kylin.core.common.JwtHelper;
import com.kylin.core.util.ComUtil;
import com.kylin.user.entity.User;
import com.kylin.user.exception.UserException;
import com.kylin.user.mapper.UserMapper;
import com.kylin.user.service.IMailService;
import com.kylin.user.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liugh123
 * @since 2018-06-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IMailService mailService;

    @Autowired
    private UserMapper userMapper;

    @Value("${file.prefix}")
    private String imgPrefix;

    /**
     * redis的key规范    业务名:表名:id  ugc:video:1
     * 1.首先通过缓存获取
     * 2.不存在将从通过数据库获取用户对象
     * 3.将用户对象写入缓存，设置缓存时间5小时
     * 4.返回对象
     * @param id
     * @return
     */
    @Override
    public User getUserById(Long id) {
        String key = "user:user:"+id;
        String json =  redisTemplate.opsForValue().get(key);
        User user = null;
        if (ComUtil.isEmpty(json)) {
            user =  userMapper.selectById(id);
            user.setAvatar(imgPrefix + user.getAvatar());
            String string  = JSON.toJSONString(user);
            redisTemplate.opsForValue().set(key, string);
            redisTemplate.expire(key, 1, TimeUnit.DAYS);
        }else {
            user = JSON.parseObject(json,User.class);
        }
        return user;
    }

    @Override
    public void addAccount(User user, String enableUrl) {
        user.setPasswd(HashUtils.encryPassword(user.getPasswd()));
        BeanHelper.onInsert(user);
        userMapper.insert(user);
        registerNotify(user.getEmail(),enableUrl);
    }

    /**
     * 发送注册激活邮件
     * @param email
     * @param enableUrl
     */
    private void registerNotify(String email, String enableUrl) {
        String randomKey = HashUtils.hashString(email) + RandomStringUtils.randomAlphabetic(10);
        redisTemplate.opsForValue().set(randomKey, email);
        redisTemplate.expire(randomKey, 1,TimeUnit.HOURS);
        String content = enableUrl +"?key="+  randomKey;
        mailService.sendSimpleMail("kylin的激活邮件", content, email);
    }

    @Override
    public boolean enable(String key) {
        String email = redisTemplate.opsForValue().get(key);
        if (ComUtil.isEmpty(email)) {
            throw new UserException(UserException.Type.USER_NOT_FOUND, "无效的key");
        }
        User updateUser = new User();
        updateUser.setEmail(email);
        updateUser.setEnable(1);
        Wrapper<User>  wa = new EntityWrapper<>();
        wa.where("email={0}",email);
        userMapper.update(updateUser,wa);
        return true;
    }

    /**
     * 校验用户名密码、生成token并返回用户对象
     * @param email
     * @param passwd
     * @return
     */
    @Override
    public User auth(String email, String passwd) {
        if (ComUtil.isEmpty(email) || ComUtil.isEmpty(passwd)) {
            throw new UserException(UserException.Type.USER_AUTH_FAIL,"User Auth Fail");
        }
        Wrapper<User>  ew = new EntityWrapper<>();
        ew.where("email={0} and  passwd ={1} and enable = 1",email,HashUtils.encryPassword(passwd));
        List<User> list = userMapper.selectList(ew);
        if (!list.isEmpty()) {
            User retUser = list.get(0);
            //设置token
            onLogin(retUser);
            return retUser;
        }
        throw new UserException(UserException.Type.USER_AUTH_FAIL,"User Auth Fail");
    }

    private void onLogin(User user) {
        String token =  JwtHelper.genToken(ImmutableMap.of("email", user.getEmail(),
                "name", user.getName(),"ts", Instant.now().getEpochSecond()+""));
        renewToken(token,user.getEmail());
        user.setToken(token);
    }

    private String renewToken(String token, String email) {
        redisTemplate.opsForValue().set(email, token);
        redisTemplate.expire(email, 30, TimeUnit.MINUTES);
        return token;
    }

    @Override
    public User getLoginedUserByToken(String token) {
        Map<String, String> map = null;
        try {
            map = JwtHelper.verifyToken(token);
        } catch (Exception e) {
            throw new UserException(UserException.Type.USER_NOT_LOGIN,"User not login");
        }
        String email =  map.get("email");
        Long expired = redisTemplate.getExpire(email);
        if (expired > 0L) {
            renewToken(token, email);
            User user = getUserByEmail(email);
            user.setToken(token);
            return user;
        }
        throw new UserException(UserException.Type.USER_NOT_LOGIN,"user not login");
    }

    @Override
    public void invalidate(String token) {
        Map<String, String> map = JwtHelper.verifyToken(token);
        redisTemplate.delete(map.get("email"));
    }

    private User getUserByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        Wrapper<User>  ew = new EntityWrapper<>();
        ew.where("email={0}",email);
        List<User> list = userMapper.selectList(ew);
        if (ComUtil.isEmpty(list)) {
            return list.get(0);
        }
        throw new UserException(UserException.Type.USER_NOT_FOUND,"User not found for " + email);
    }
}
