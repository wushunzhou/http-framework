package com.dora.httpframework.core.notification;

import com.dora.httpframework.core.base.RepResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: dora
 * @Date: 2019/9/16 15:04.
 */
@Service
public class RobotTalkNotificationHandler extends NotificationHandler {
    // FIXME: 禁止Test时直接注入调用
    @Autowired(required = false)
    private RobotTalkNotificationSererImpl robotTalkNotificationSererImpl;

    @Override
    public void notification() {
        RobotTalkNotificationRequest flyBookTalkRequest = RobotTalkNotificationRequest.builder()
                .msg_type(RobotTalkNotificationSererImpl.MsgType.text)
                .content(RobotTalkNotificationRequest.Content.builder().text("开始拨测，请检查！").build())
                .build();
        robotTalkNotificationSererImpl.notification(flyBookTalkRequest);
    }

    @Override
    public void notification(NotificationRequest request) {
        robotTalkNotificationSererImpl.notification(request);
    }

    @Override
    public RepResult notificationResult(NotificationRequest request) {
        return robotTalkNotificationSererImpl.notificationResult(request);
    }
}
