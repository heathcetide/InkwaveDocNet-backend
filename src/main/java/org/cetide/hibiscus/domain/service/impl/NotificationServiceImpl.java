package org.cetide.hibiscus.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cetide.hibiscus.infrastructure.persistence.mapper.NotificationMapper;
import org.cetide.hibiscus.domain.model.aggregate.Notification;
import org.cetide.hibiscus.domain.service.NotificationService;
import org.springframework.stereotype.Service;

/**
 * 消息通知实现类
 *
 * @author heathcetide
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Override
    public void sendNotification(Notification notification) {
        notificationMapper.insert(notification);
    }
}
