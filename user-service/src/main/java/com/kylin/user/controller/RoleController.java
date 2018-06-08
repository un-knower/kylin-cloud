package com.kylin.user.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.kylin.rest.base.PublicResult;
import com.kylin.rest.base.PublicResultConstant;
import com.kylin.rest.entity.Role;
import com.kylin.rest.service.IRoleService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liugh123
 * @since 2018-05-03
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @GetMapping(value = "/all")
    @RequiresAuthentication
    public PublicResult<List<Role>> findAll() {
        EntityWrapper<Role> ew = new EntityWrapper<>();
        List<Role> result = roleService.selectList(ew);
        return new PublicResult<>(PublicResultConstant.SUCCESS, result);
    }

}

