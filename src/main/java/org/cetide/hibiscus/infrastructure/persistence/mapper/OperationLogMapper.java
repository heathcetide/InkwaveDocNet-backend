package org.cetide.hibiscus.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.cetide.hibiscus.domain.model.aggregate.OperationLog;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}
