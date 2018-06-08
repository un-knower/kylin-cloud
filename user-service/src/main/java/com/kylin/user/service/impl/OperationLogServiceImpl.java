package com.kylin.user.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.kylin.user.entity.OperationLog;
import com.kylin.user.mapper.OperationLogMapper;
import com.kylin.user.service.IOperationLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author liugh123
 * @since 2018-05-08
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

}
