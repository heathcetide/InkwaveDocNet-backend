package org.cetide.hibiscus.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cetide.hibiscus.domain.model.aggregate.OrgInvite;
import org.cetide.hibiscus.infrastructure.persistence.mapper.OrgInviteMapper;
import org.cetide.hibiscus.domain.service.OrgInviteService;
import org.cetide.hibiscus.infrastructure.utils.InviteCodeUtil;
import org.cetide.hibiscus.infrastructure.utils.SnowflakeIdGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * OrgInvite 服务实现类
 * @author Hibiscus-code-generate
 */
@Service
public class OrgInviteServiceImpl extends ServiceImpl<OrgInviteMapper, OrgInvite> implements OrgInviteService {

    @Override
    public OrgInvite createInvite(Long organizationId, Long inviterId, String role, Integer maxUses, LocalDateTime expiresAt) {
        OrgInvite invite = new OrgInvite();
        invite.setId(SnowflakeIdGenerator.nextId());
        invite.setInviterId(inviterId);
        invite.setOrganizationId(organizationId);
        invite.setRole(role);
        invite.setMaxUses(maxUses);
        invite.setUsedCount(0);
        invite.setExpiresAt(expiresAt);
        save(invite); // 插入后自动生成ID
        // 生成短码
        String shortCode = InviteCodeUtil.encode(invite.getId());
        invite.setInviteCode(shortCode);
        updateById(invite); // 保存邀请码字段
        return invite;
    }

    @Override
    public OrgInvite getOrgIdByCode(String code) {
        LambdaQueryWrapper<OrgInvite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrgInvite::getInviteCode, code);
        return getOne(queryWrapper);
    }

//    public String buildInviteLink(String code) {
//        return "https://yourdomain.com/invite/" + code;
//    }
//
//    public BufferedImage generateQRCode(String content) throws Exception {
//        return QRCode.from(content).withSize(250, 250).to(ImageType.PNG).getImage(); // 使用 zxing 或 qrgen
//    }
//
//    public OrgInvite validateInvite(String code) {
//        Long id = InviteCodeUtil.decode(code);
//        OrgInvite invite = getById(id);
//
//        if (invite == null) throw new RuntimeException("邀请码不存在");
//        if (invite.getExpiresAt() != null && invite.getExpiresAt().isBefore(LocalDateTime.now())) {
//            throw new RuntimeException("邀请码已过期");
//        }
//        if (invite.getMaxUses() != null && invite.getUsedCount() >= invite.getMaxUses()) {
//            throw new RuntimeException("邀请码已达最大使用次数");
//        }
//
//        return invite;
//    }
//
//
//    @Transactional
//    public void acceptInvite(String code, Long userId) {
//        OrgInvite invite = validateInvite(code);
//
//        // 加入组织逻辑
//        organizationMemberService.addUserToOrganization(invite.getOrganizationId(), userId, invite.getRole());
//
//        // 更新使用次数
//        invite.setUsedCount(invite.getUsedCount() + 1);
//        updateById(invite);
//    }
//
//    @GetMapping("/{code}/qrcode")
//    public void getQrCode(@PathVariable String code, HttpServletResponse response) throws Exception {
//        String link = buildInviteLink(code);
//        BufferedImage qr = generateQRCode(link);
//
//        response.setContentType("image/png");
//        ImageIO.write(qr, "PNG", response.getOutputStream());
//    }

}
