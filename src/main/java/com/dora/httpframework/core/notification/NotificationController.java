package com.dora.httpframework.core.notification;

import com.dora.httpframework.core.base.RepResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Describe 机器人通知
 * @Author dora 1.0.1
 **/
@Slf4j
@RestController
@RequestMapping("/robot")
public class NotificationController {

    @PostMapping("/sendMsg")
    public RepResult notificationMsg(String msg) {
        NotificationContextHandler handlerContext = new NotificationContextHandler();
        RobotTalkNotificationRequest flyBookTalkRequest = RobotTalkNotificationRequest.builder()
                .msg_type(RobotTalkNotificationSererImpl.MsgType.text)
                .content(RobotTalkNotificationRequest.Content.builder().text(msg).build())
                .build();
        return handlerContext.getInstance("1").notificationResult(flyBookTalkRequest);
    }

}
