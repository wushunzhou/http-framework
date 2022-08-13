package com.dora.httpframework.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * @Describe ContextClosedEventListener
 * @Author dora 1.0.1
 **/
@Component
@Slf4j
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("ContextClosedEventListener Start ....");
    }
}
