package com.kylin.user.controller;

import com.kylin.user.common.RestResponse;
import com.kylin.user.entity.User;
import com.kylin.user.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private IUserService userService;

  /**
   * 查询
   * @param id
   * @return
   */
  @GetMapping("getById")
  public RestResponse<User> getUserById(Long id){
    User user = userService.getUserById(id);
    return RestResponse.success(user);
  }

  /**
   * 注册用户
   * @param user
   * @return
   */
  @PostMapping("add")
  public RestResponse<User> add(@RequestBody User user){
    userService.addAccount(user,user.getEnableUrl());
    return RestResponse.success();
  }

  /**
   * 邮箱激活key的验证
   */
  @GetMapping("enable")
  public RestResponse<Object> enable(String key){
    userService.enable(key);
    return RestResponse.success();
  }

  /**
   * 鉴权
   * @param user
   * @return
   */

  @PostMapping("auth")
  public RestResponse<User> auth(@RequestBody User user){
    User finalUser = userService.auth(user.getEmail(),user.getPasswd());
    return RestResponse.success(finalUser);
  }

  @GetMapping("get")
  public RestResponse<User> getUser(String token){
    User finalUser = userService.getLoginedUserByToken(token);
    return RestResponse.success(finalUser);
  }



  @DeleteMapping("logout")
  public RestResponse<Object> logout(String token){
    userService.invalidate(token);
    return RestResponse.success();
  }


}
