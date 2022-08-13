package com.dora.httpframework.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.RequestHandledEvent;

/**
 * @Describe 前端监听
 * @Author dora 1.0.1
 **/
@Component
@Slf4j
public class RequestHandledEventListener implements ApplicationListener<RequestHandledEvent> {

    @Override
    public void onApplicationEvent(RequestHandledEvent event) {
        log.info("Http Request Already End ....");
    }
}
