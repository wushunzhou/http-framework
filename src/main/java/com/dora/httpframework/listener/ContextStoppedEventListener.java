package com.dora.httpframework.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

/**
 * @Describe ContextStoppedEventListener
 * @Author dora 1.0.1
 **/
@Component
@Slf4j
public class ContextStoppedEventListener implements ApplicationListener<ContextStoppedEvent> {


    @Override
    public void onApplicationEvent(ContextStoppedEvent event) {
        log.info("ContextStoppedEventListener Start ....");
    }
}
