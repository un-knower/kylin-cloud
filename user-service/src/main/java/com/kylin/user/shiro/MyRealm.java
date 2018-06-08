package com.kylin.user.shiro;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.kylin.core.common.Constant;
import com.kylin.user.config.SpringContextBean;
import com.kylin.user.entity.Menu;
import com.kylin.user.entity.User;
import com.kylin.user.entity.UserToRole;
import com.kylin.user.exception.UserException;
import com.kylin.user.service.IMenuService;
import com.kylin.user.service.IUserService;
import com.kylin.user.service.IUserToRoleService;
import com.kylin.core.util.ComUtil;
import com.kylin.core.common.JwtHelper;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liugh
 * @since 2018-05-03
 */
public class MyRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(MyRealm.class);
    private IUserService userService;
    private IUserToRoleService userToRoleService;
    private IMenuService menuService;
    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (userToRoleService == null) {
            this.userToRoleService = SpringContextBean.getBean(IUserToRoleService.class);
        }
        if (menuService == null) {
            this.menuService = SpringContextBean.getBean(IMenuService.class);
        }

        String username = JwtHelper.getUsername(principals.toString());
        EntityWrapper<User> ew = new EntityWrapper<>();
        ew.where("account={0}", username);
        User user= userService.selectOne(ew);
        UserToRole userToRole = userToRoleService.selectByUserId(user.getId());

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        ArrayList<String> pers = new ArrayList<>();
        List<Menu> menuList = menuService.findMenuByRoleId(userToRole.getRoleId(), Constant.ENABLE);
        for (Menu per : menuList) {
            if (!ComUtil.isEmpty(per.getCode())) {
                if (!ComUtil.isEmpty(per.getCode().replace(" ", ""))) {
                    pers.add(per.getCode());
                }
            }
        }
        Set<String> permission = new HashSet<>(pers);
        simpleAuthorizationInfo.addStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws UserException {
        if (userService == null) {
            this.userService = SpringContextBean.getBean(IUserService.class);
        }
        String token = (String) auth.getCredentials();

        // 解密获得username，用于和数据库进行对比
        String username = JwtHelper.getUsername(token);
        if (username == null) {
            throw new UserException("token invalid");
        }
        EntityWrapper<User> ew = new EntityWrapper<>();
        ew.where("account={0}", username);
        User userBean= userService.selectOne(ew);
        if (userBean == null) {
            throw new UserException("User didn't existed!");
        }
//        if (! JwtHelper.verify(token, username, userBean.getPassWord())) {
//            throw new UserException("Username or password error");
//        }
        return new SimpleAuthenticationInfo(token, token, this.getName());
    }
}
