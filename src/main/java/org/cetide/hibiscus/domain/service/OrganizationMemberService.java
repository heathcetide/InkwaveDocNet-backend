package org.cetide.hibiscus.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cetide.hibiscus.domain.model.aggregate.OrganizationMember;
import org.cetide.hibiscus.interfaces.rest.dto.InviteMemberRequest;
import org.cetide.hibiscus.interfaces.rest.dto.OrganizationMemberVO;
import org.cetide.hibiscus.interfaces.rest.dto.OrganizationVO;

import java.util.List;

/**
 * OrganizationMember 服务接口
 * @author Hibiscus-code-generate
 */
public interface OrganizationMemberService extends IService<OrganizationMember> {

    /**
     * 根据用户id获取用户加入的组织
     * @param userId 用户id
     * @return 组织列表
     */
    List<OrganizationVO> getOrganizationsByUserId(Long userId);
}
