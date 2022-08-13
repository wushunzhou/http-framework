package com.dora.httpframework.core.notification;

import com.dora.httpframework.core.headerfilter.BaseRestLogFilter;
import com.dora.httpframework.core.request.RequestHttpBuilder;
import com.dora.httpframework.utils.JSONSerializer;
import com.dora.httpframework.core.base.RepResult;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.testng.annotations.Test;

/**
 * @Author: dora
 * @Date: 2019/9/12 14:35.
 * 机器人通知 SererImpl
 */
@Service
@Slf4j
public class RobotTalkNotificationSererImpl implements BaseNotificationServer {
    @Value("${notification.robot.url}")
    private String url;

    @Override
    public void notification(NotificationRequest request) {
        String requestJson = JSONSerializer.serialize(request);
        log.info("机器人通知=============");
        RequestHttpBuilder.create().post(requestJson, url, new BaseRestLogFilter());
    }

    @Override
    public RepResult notificationResult(NotificationRequest request) {
        String requestJson = JSONSerializer.serialize(request);
        log.info("机器人通知=============");
        Response res = RequestHttpBuilder.create().post(requestJson, url, new BaseRestLogFilter());
        if (res.getStatusCode() != 200) {
            return RepResult.notificationFail(res.asString());
        }
        return RepResult.success(res.asString());
    }

    public enum MsgType {
        text, link, markdown, @Deprecated actionCard, @Deprecated feedCard
    }

    //  测试机器人通知
    @Test
    private void NotificationTest() {
//        text 格式
//        RobotTalkNotificationRequest robotTalkRequest = RobotTalkNotificationRequest.builder()
//                .msg_type(RobotTalkNotificationSererImpl.MsgType.text)
//                .content(RobotTalkNotificationRequest.Content.builder().text("测试用例运行完毕，请检查！").build())
//                .build();
//        link 格式
//        RobotTalkNotificationRequest robotTalkRequest = RobotTalkNotificationRequest.builder()
//                .msg_type(MsgType.text)
//                .content(RobotTalkNotificationRequest.Content.builder().text("测试用例运行完毕，请检查！").build())
//                .build();
//
//        notification(robotTalkRequest);

    }
}
