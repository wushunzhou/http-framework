package com.dora.httpframework.core.notification;

import com.dora.httpframework.core.base.RepResult;

/**
 * @Author: dora
 * @Date: 2019/9/16 14:58.
 */
public abstract class NotificationHandler {
    public abstract void notification();
    public abstract void notification(NotificationRequest request);
    public abstract RepResult notificationResult(NotificationRequest request);
}
