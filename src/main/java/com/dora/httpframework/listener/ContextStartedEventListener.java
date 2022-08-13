package com.dora.httpframework.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

/**
 * @Describe ContextStartedEventListener
 * @Author dora 1.0.1
 **/
@Component
@Slf4j
public class ContextStartedEventListener implements ApplicationListener<ContextStartedEvent> {
    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        log.info("ContextStartedEventListener Start ....");
    }
}
