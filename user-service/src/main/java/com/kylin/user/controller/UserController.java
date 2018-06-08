package com.kylin.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.kylin.core.common.RestCode;
import com.kylin.core.common.RestResponse;
import com.kylin.user.annotation.ValidationParam;
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
   *
   {
   "name":"liugh测试11",
   "phone":"17765071663",
   "email":"66864662@qq.com",
   "aboutme":"111",
   "passwd":"123456",
   "avatar":"/12345215/user.jpg",
   "type":1,
   "create_time":"2018-06-08",
   "enable":0,
   "agencyId":0,
   "enableUrl":"http://localhost:8083/user/enable"
   }
   * @param user
   * @return
   */
  @PostMapping("addByEmail")
  public RestResponse<User> addByEmail(@ValidationParam("userName,passWord,rePassWord,realName,telephone,sex,email,unit,cityId,deptId")
                                         @RequestBody JSONObject requestJson){
    userService.addAccount(user,"http://localhost:8083/user/enable");
    return RestResponse.success();
  }

  @PostMapping("addByPhone")
  public RestResponse<User> addByPhone(@RequestBody User user){
//    userService.addAccount(user,user.getEnableUrl());
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
   *
   {
   "email":"66864662@qq.com",
   "passwd":"123456"
   }
   * @param user
   * @return
   */

  @PostMapping("auth")
  public RestResponse<User> auth(@RequestBody User user){
    User finalUser = userService.auth(user.getEmail(),user.getPasswd());
    return RestResponse.error(RestCode.ILLEGAL_PARAMS);
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
