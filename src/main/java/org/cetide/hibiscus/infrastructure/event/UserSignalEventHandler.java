package org.cetide.hibiscus.infrastructure.event;

import com.hibiscus.signal.Signals;
import com.hibiscus.signal.core.SignalContext;
import com.hibiscus.signal.spring.anno.SignalHandler;
import org.cetide.hibiscus.common.notification.SendNotifyStrategy;
import org.cetide.hibiscus.common.notification.impl.SendNotificationImpl;
import org.cetide.hibiscus.domain.model.aggregate.Notification;
import org.cetide.hibiscus.domain.model.aggregate.User;
import org.cetide.hibiscus.domain.model.enums.NotificationLevel;
import org.cetide.hibiscus.domain.model.enums.NotificationType;
import org.cetide.hibiscus.domain.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

import static org.cetide.hibiscus.common.constants.UserEventConstants.EVENT_INTER_MEDIATE_USER;

@Component
public class UserSignalEventHandler {

    private final SendNotifyStrategy sendNotifyStrategy;
    private final SendNotificationImpl sendNotificationService;

    private final NotificationService notificationService;

    private static final Logger log = LoggerFactory.getLogger(UserSignalEventHandler.class);
    private final Signals signals;

    public UserSignalEventHandler(@Qualifier("emailStrategy") SendNotifyStrategy sendNotifyStrategy, SendNotificationImpl sendNotificationService, NotificationService notificationService, Signals signals) {
        this.sendNotifyStrategy = sendNotifyStrategy;
        this.sendNotificationService = sendNotificationService;
        this.notificationService = notificationService;
        this.signals = signals;
    }


    @SignalHandler(value = "user.login", target = UserSignalEventHandler.class, methodName = "handleLogin", timeoutMs = 100000)
    public void handleLogin(SignalContext signalContext) throws Exception {
        User user = (User) signalContext.getIntermediateValues().get(EVENT_INTER_MEDIATE_USER);
        try {
            // 发送欢迎邮件
            sendNotificationService.setSendNotifyStrategy(sendNotifyStrategy);
            sendNotificationService.sendWelcomeEmail(user.getUsername(), user.getEmail());
        } catch (Exception e) {
            log.error("发送邮件失败："+e.getMessage());
        }
    }

    @SignalHandler(value = "user.login", target = UserSignalEventHandler.class, methodName = "sendWelcomeEmail", timeoutMs = 100000)
    public void sendWelcomeEmail(SignalContext signalContext){
        User user = (User) signalContext.getIntermediateValues().get(EVENT_INTER_MEDIATE_USER);
        notificationService.sendNotification(new Notification.Builder()
                .userId(user.getId())
                .type(NotificationType.SYSTEM)
                .title("欢迎登录")
                .content("欢迎您今日登录, 请开始今日的墨协使用之旅吧！")
                .level(NotificationLevel.WELCOME)
                .businessId(signalContext.getTraceId())
                .businessType("user.login")
                .sendTime(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }
}
