package org.cetide.hibiscus.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cetide.hibiscus.infrastructure.persistence.mapper.OperationLogMapper;
import org.cetide.hibiscus.domain.model.aggregate.OperationLog;
import org.cetide.hibiscus.domain.service.OperationLogService;
import org.springframework.stereotype.Service;

/**
 * 日志服务实现类
 *
 * @author heathcetide
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {
}
