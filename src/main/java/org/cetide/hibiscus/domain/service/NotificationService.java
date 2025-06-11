package org.cetide.hibiscus.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cetide.hibiscus.common.anno.Loggable;
import org.cetide.hibiscus.domain.model.aggregate.Notification;
import org.cetide.hibiscus.domain.model.enums.LogType;

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
}
