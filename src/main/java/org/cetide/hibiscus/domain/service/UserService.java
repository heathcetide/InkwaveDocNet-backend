package org.cetide.hibiscus.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cetide.hibiscus.domain.model.aggregate.User;
import org.cetide.hibiscus.infrastructure.persistence.entity.UserEntity;

/**
 * User 服务接口
 * @author Hibiscus-code-generate
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 重置密码
     */
    void resetPassword(String emailOrPhone, String newPassword);

    /**
     * 根据邮箱获取用户信息
     */
    UserEntity getUserByEmail(String email);
}

