package com.dora.httpframework.core.notification;

import com.dora.httpframework.core.base.RepResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: dora
 * @Date: 2019/9/16 15:00.
 */
@Service
@Slf4j
public class MailNotificationHandler extends NotificationHandler {

    // FIXME: 禁止Test时直接注入调用
    @Autowired(required = false)
    private MailNotificationServerImpl mailNotificationServerImpl;

    @Override
    public void notification() {
        MailNotificationRequest mailNotificationRequest = MailNotificationRequest.builder()
                .subject("测试用例运行完毕")
                .context("测试用例运行完毕，请检查！")
                .build();
        try {
            mailNotificationServerImpl.notification(mailNotificationRequest);
        } catch (Exception e) {
            log.error("邮件发送失败", e);
        }
    }

    @Override
    public void notification(NotificationRequest request) {
        try {
            mailNotificationServerImpl.notification(request);
        } catch (Exception e) {
            log.error("邮件发送失败", e);
        }
    }

    @Override
    public RepResult notificationResult(NotificationRequest request) {
        return notificationResult(request);
    }
}
