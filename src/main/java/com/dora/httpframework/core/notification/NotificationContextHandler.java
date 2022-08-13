package com.dora.httpframework.core.notification;

import com.dora.httpframework.utils.SpringContext;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Data 2022/2/23
 * @Param
 **/
public class NotificationContextHandler {
    private static Map<String, Class> handlerMap = new HashMap<>();

    static {
        handlerMap.put(Channel.RobotTalk.getType(), RobotTalkNotificationHandler.class);
        handlerMap.put(Channel.Mail.getType(), MailNotificationHandler.class);
    }

    public NotificationHandler getInstance(String type) {
        Class clazz = handlerMap.get(type);

        if (clazz == null) {
            throw new IllegalArgumentException("not found handler for type:" + type);
        }

        return SpringContext.getBean((Class<NotificationHandler>) clazz);
    }

    public List<NotificationHandler> getInstanceAll() {
        List<NotificationHandler> handlers = Lists.newArrayList();

        handlerMap.forEach((k, v) -> handlers.add(SpringContext.getBean((Class<NotificationHandler>) v)));

        return handlers;
    }

    public enum Channel {
        No("0"), RobotTalk("1"), Mail("2"), ALL("9");

        private String type;

        Channel(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }
    }
}
