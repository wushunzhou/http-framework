package com.dora.httpframework.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Describe ContextRefreshedEvent
 * @Author dora 1.0.1
 **/
@Component
@Slf4j
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("ContextRefreshedEvent Listener Start ...... ");

    }


}
