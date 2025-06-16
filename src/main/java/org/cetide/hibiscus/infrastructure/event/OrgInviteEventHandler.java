package org.cetide.hibiscus.infrastructure.event;

import com.hibiscus.signal.core.SignalContext;
import com.hibiscus.signal.spring.anno.SignalHandler;
import org.cetide.hibiscus.domain.model.aggregate.Notification;
import org.cetide.hibiscus.domain.model.aggregate.User;
import org.cetide.hibiscus.domain.model.enums.NotificationLevel;
import org.cetide.hibiscus.domain.model.enums.NotificationType;
import org.cetide.hibiscus.domain.service.NotificationService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.cetide.hibiscus.common.constants.UserEventConstants.EVENT_INTER_MEDIATE_USER;

@Component
public class OrgInviteEventHandler {

    private final NotificationService notificationService;

    public OrgInviteEventHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @SignalHandler(value = "invite.create", target = OrgInviteEventHandler.class, methodName= "handleCreateInvite")
    public void handleCreateInvite(SignalContext signalContext) {
        User user = (User) signalContext.getIntermediateValues().get(EVENT_INTER_MEDIATE_USER);
        notificationService.sendNotification(new Notification.Builder()
                .userId(user.getId())
                .type(NotificationType.SYSTEM)
                .title("已成功生成团队邀请链接")
                .content("您已为当前团队生成了一条专属邀请链接。可通过二维码或链接邀请他人加入团队，共同协作。  " +
                        "请注意：该邀请码支持设置使用次数及有效期，您可在团队管理页面随时查看和管理当前的所有邀请记录。")
                .level(NotificationLevel.NORMAL)
                .businessId(signalContext.getTraceId())
                .businessType("invite.create")
                .sendTime(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }

    @SignalHandler(value = "invite.use", target = OrgInviteEventHandler.class, methodName= "handleInviteUse")
    public void handleInviteUse(SignalContext signalContext) {
        User user = (User) signalContext.getIntermediateValues().get(EVENT_INTER_MEDIATE_USER);
        notificationService.sendNotification(new Notification.Builder()
                .userId(user.getId())
                .type(NotificationType.SYSTEM)
                .title("欢迎加入团队！")
                .content("您已成功通过邀请码加入团队。现在可以访问团队空间、参与协作、查看项目资料。   " +
                        "建议您完善个人资料、了解团队成员，并在“团队”模块中查看权限与职责。如有任何问题，可联系管理员。祝您使用愉快！")
                .level(NotificationLevel.IMPORTANT)
                .businessId(signalContext.getTraceId())
                .businessType("invite.use")
                .sendTime(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }
}
