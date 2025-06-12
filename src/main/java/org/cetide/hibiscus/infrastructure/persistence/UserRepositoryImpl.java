package org.cetide.hibiscus.infrastructure.persistence;

import org.cetide.hibiscus.domain.model.aggregate.User;
import org.cetide.hibiscus.domain.repository.UserRepository;
import org.cetide.hibiscus.infrastructure.persistence.converter.UserConverter;
import org.cetide.hibiscus.infrastructure.persistence.entity.UserEntity;
import org.cetide.hibiscus.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    public UserRepositoryImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void save(User user) {
        UserEntity entity = UserConverter.toEntity(user);
        if (entity.getId() == null) {
            userMapper.insert(entity);
            user.setId(entity.getId()); // 写回 ID
        } else {
            userMapper.updateById(entity);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        UserEntity entity = userMapper.selectById(id);
        return Optional.ofNullable(UserConverter.toAggregate(entity));
    }
}
