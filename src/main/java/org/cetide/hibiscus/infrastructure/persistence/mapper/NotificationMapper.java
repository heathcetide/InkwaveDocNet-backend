package org.cetide.hibiscus.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.cetide.hibiscus.domain.model.aggregate.Notification;

@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
}
