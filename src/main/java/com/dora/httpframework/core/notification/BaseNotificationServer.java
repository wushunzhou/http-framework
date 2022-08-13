package com.dora.httpframework.core.notification;

import com.dora.httpframework.core.base.RepResult;

/**
 * @Author: dora
 * @Date: 2019/9/16 13:25.
 */
public interface BaseNotificationServer {
    void notification(NotificationRequest request) throws Exception;

    RepResult notificationResult(NotificationRequest request) throws Exception;
}
