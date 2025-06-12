package org.cetide.hibiscus.application.service;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cetide.hibiscus.application.command.CreateUserCommand;
import org.cetide.hibiscus.application.command.DeleteUserCommand;
import org.cetide.hibiscus.application.command.UpdateUserCommand;
import org.cetide.hibiscus.application.dto.UserDTO;
import org.cetide.hibiscus.common.request.PageRequest;
import org.cetide.hibiscus.domain.model.aggregate.User;
import org.cetide.hibiscus.domain.repository.UserRepository;
import org.cetide.hibiscus.infrastructure.persistence.converter.UserConverter;
import org.cetide.hibiscus.infrastructure.persistence.entity.UserEntity;
import org.cetide.hibiscus.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class UserApplicationService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserApplicationService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public void createUser(CreateUserCommand command) {
        User user = new User(
                null,
                command.getUsername(),
                command.getEmail(),
                command.getPassword(),
                command.getAvatarUrl(),
                "ACTIVE",
                LocalDateTime.now(),
                LocalDateTime.now(),
                false
        );
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(UpdateUserCommand command) {
        User user = userRepository.findById(command.getId())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        user.update(command.getUsername(), command.getEmail(), command.getAvatarUrl(), command.getStatus());
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(DeleteUserCommand command) {
        User user = userRepository.findById(command.getId())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        user.markDeleted();
        userRepository.save(user);
    }


    public IPage<UserDTO> getUserPage(PageRequest request) {
        Page<UserEntity> page = new Page<>(request.getPage(), request.getSize());
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();

        if (StringUtils.hasText(request.getKeyword())) {
            wrapper.like("username", request.getKeyword());
        }

        return userMapper.selectPage(page, wrapper)
                .convert(UserConverter::toDTO); // 返回 Page<UserDTO> 也是 IPage<UserDTO>
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserConverter::toEntity)      // 从聚合转为实体
                .map(UserConverter::toDTO)         // 再转为 DTO
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }
}