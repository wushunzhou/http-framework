package com.dora.httpframework.core.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: dora
 * @Date: 2019/9/12 15:57.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RobotTalkNotificationRequest extends NotificationRequest{

    private RobotTalkNotificationSererImpl.MsgType msg_type;

    private ActionCard actionCard;

    private At at;

    private FeedCard feedCard;

    private Link link;

    private Markdown markdown;

    private Content content;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class At {
        private List<String> atMobiles;

        Boolean isAtAll;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Content {
        private String text;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Markdown {
        private String text;

        private String title;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Link {

        /**
         * 点击消息跳转的URL
         */
        private String messageUrl;

        /**
         * 图片URL
         */
        private String picUrl;

        /**
         * 消息内容。如果太长只会部分展示
         */
        private String text;

        /**
         * 标题
         */
        private String title;
    }

    @Data
    @Builder
    public static class FeedCard {
    }

    @Data
    @Builder
    public static class ActionCard {
    }
}
