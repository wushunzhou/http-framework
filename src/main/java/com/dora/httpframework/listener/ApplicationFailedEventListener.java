package com.dora.httpframework.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Describe ApplicationFailedEventListener
 * @Author dora 1.0.1
 **/
@Component
@Slf4j
public class ApplicationFailedEventListener implements ApplicationListener<ApplicationFailedEvent> {
    @Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
        log.error("Application Failed ......");
    }
}
