package org.cetide.hibiscus.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cetide.hibiscus.domain.model.aggregate.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * User Mapper 接口
 * @author Hibiscus-code-generate
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
