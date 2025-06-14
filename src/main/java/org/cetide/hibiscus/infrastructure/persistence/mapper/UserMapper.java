package org.cetide.hibiscus.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cetide.hibiscus.domain.model.aggregate.User;
import org.apache.ibatis.annotations.Mapper;
import org.cetide.hibiscus.infrastructure.persistence.entity.UserEntity;

/**
 * User Mapper 接口
 * @author Hibiscus-code-generate
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    /**
     * 根据用户名检查用户是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(@Param("username") String username);

    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(@Param("email") String email);

    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户
     */
    User selectByEmail(String email);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    User selectByUsername(@Param("username") String username);
}
