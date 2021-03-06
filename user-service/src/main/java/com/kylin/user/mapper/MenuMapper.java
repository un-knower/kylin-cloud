package com.kylin.user.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.kylin.user.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liugh123
 * @since 2018-05-03
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> findMenuByRoleId(@Param("roleId") Integer roleId, @Param("status") Integer status);
}
