package org.cetide.hibiscus.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cetide.hibiscus.domain.model.aggregate.Webhook;
import org.apache.ibatis.annotations.Mapper;

/**
 * Webhook Mapper 接口
 * @author Hibiscus-code-generate
 */
@Mapper
public interface WebhookMapper extends BaseMapper<Webhook> {

}
