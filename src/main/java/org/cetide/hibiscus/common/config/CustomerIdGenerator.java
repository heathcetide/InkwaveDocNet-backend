package org.cetide.hibiscus.common.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.cetide.hibiscus.infrastructure.utils.IdGenerator;
import org.springframework.stereotype.Component;

@Component
public class CustomerIdGenerator implements IdentifierGenerator {
    @Override
    public Long nextId(Object entity) {
        return IdGenerator.generateId();
    }
}
