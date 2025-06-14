package org.cetide.hibiscus.infrastructure.persistence.converter;

import org.cetide.hibiscus.application.dto.UserDTO;
import org.cetide.hibiscus.domain.model.aggregate.User;
import org.cetide.hibiscus.infrastructure.persistence.entity.UserEntity;
import org.cetide.hibiscus.interfaces.rest.dto.UserVO;

public class UserConverter {

    public static UserEntity toEntity(User user) {
        if (user == null) return null;
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setAvatarUrl(user.getAvatarUrl());
        entity.setStatus(user.getStatus());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        entity.setDeleted(user.getDeleted());
        return entity;
    }

    public static User toAggregate(UserEntity entity) {
        if (entity == null) return null;
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getAvatarUrl(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                Boolean.TRUE.equals(entity.getDeleted())
        );
    }

    public static UserDTO toDTO(UserEntity entity) {
        if (entity == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setAvatarUrl(entity.getAvatarUrl());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public UserVO toUserVO(UserEntity entity) {
        return new UserVO(
                entity.getUsername(),
                entity.getEmail(),
                entity.getAvatarUrl(),
                entity.getStatus(),
                entity.getThemeDark(),
                entity.getEmailNotifications(),
                entity.getLanguage(),
                entity.getBio()
        );
    }
}