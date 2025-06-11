package org.cetide.hibiscus.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cetide.hibiscus.domain.model.aggregate.Webhook;
import org.cetide.hibiscus.domain.service.WebhookService;
import org.cetide.hibiscus.infrastructure.persistence.mapper.WebhookMapper;
import org.springframework.stereotype.Service;

/**
 * Webhook 服务实现类
 * @author Hibiscus-code-generate
 */
@Service
public class WebhookServiceImpl extends ServiceImpl<WebhookMapper, Webhook> implements WebhookService {

}
