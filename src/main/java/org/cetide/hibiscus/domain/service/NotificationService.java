package org.cetide.hibiscus.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cetide.hibiscus.common.anno.Loggable;
import org.cetide.hibiscus.domain.model.aggregate.Notification;
import org.cetide.hibiscus.domain.model.enums.LogType;
import org.cetide.hibiscus.domain.model.enums.NotificationType;

import java.util.List;

/**
 * 消息通知服务
 *
 * @author heathcetide
 */
public interface NotificationService extends IService<Notification> {

    /**
     * 发送站内消息
     */
    @Loggable(type = LogType.SYSTEM_MESSAGE, value = "发送通知")
    void sendNotification(Notification notification);

    /**
     * 标记单条通知为已读
     */
    @Loggable(type = LogType.SYSTEM_MESSAGE, value = "标记单条通知为已读")
    void markAsRead(Long userId, Long notificationId);

    /**
     * 将所有通知标记为已读
     */
    @Loggable(type = LogType.SYSTEM_MESSAGE, value = "将所有通知标记为已读")
    void markAllAsRead(Long userId);

    /**
     * 删除单条通知
     */
    @Loggable(type = LogType.SYSTEM_MESSAGE, value = "删除单条通知")
    void deleteByUserAndId(Long userId, Long notificationId);

    /**
     * 获取当前用户通知列表
     */
    @Loggable(type = LogType.SYSTEM_MESSAGE, value = "获取当前用户通知列表")
    void clearAllByUserId(Long userId);

    /**
     * 获取当前用户通知列表
     */
    @Loggable(type = LogType.SYSTEM_MESSAGE, value = "获取当前用户通知列表")
    IPage<Notification> getNotificationsByUserIdPaged(Long userId, int page, int size, NotificationType type);

    /**
     * 获取当前用户未读通知数量
     */
    @Loggable(type = LogType.SYSTEM_MESSAGE, value = "获取当前用户未读通知数量")
    long countUnreadByUserId(Long userId);
}
