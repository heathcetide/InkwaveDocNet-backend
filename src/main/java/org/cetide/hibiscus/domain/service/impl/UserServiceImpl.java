package org.cetide.hibiscus.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cetide.hibiscus.common.exception.BusinessException;
import org.cetide.hibiscus.domain.model.aggregate.User;
import org.cetide.hibiscus.domain.service.UserService;
import org.cetide.hibiscus.infrastructure.persistence.entity.UserEntity;
import org.cetide.hibiscus.infrastructure.persistence.mapper.UserMapper;
import org.cetide.hibiscus.infrastructure.utils.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery;
import static org.cetide.hibiscus.domain.model.enums.ResponseCodeEnum.SYSTEM_ERROR;

/**
 * User 服务实现类
 * @author Hibiscus-code-generate
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {


    @Override
    public void resetPassword(String email, String newPassword) {
        UserEntity user = lambdaQuery()
                .eq(UserEntity::getEmail, email)
                .or()
                .one();
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        user.setPassword(PasswordEncoder.encode(newPassword)); // 加密密码
        updateById(user);
    }


    /**
     * 通过 Email 获取用户信息
     */
    @Override
    public UserEntity getUserByEmail(String email) {
        // 使用 LambdaQueryWrapper 来构建查询条件
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getEmail, email);  // 使用 email 字段查询

        // 执行查询
        UserEntity user = this.getOne(queryWrapper);  // 使用 MyBatis-Plus 提供的 getOne() 方法查询

        // 如果未找到用户，返回 null 或抛出异常
        if (user == null) {
            throw new BusinessException(SYSTEM_ERROR.code(), "用户不存在");
        }

        return user;
    }
}
