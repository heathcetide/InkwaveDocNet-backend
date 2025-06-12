package org.cetide.hibiscus.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cetide.hibiscus.domain.model.aggregate.User;
import org.cetide.hibiscus.domain.service.UserService;
import org.cetide.hibiscus.infrastructure.persistence.entity.UserEntity;
import org.cetide.hibiscus.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * User 服务实现类
 * @author Hibiscus-code-generate
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

}
