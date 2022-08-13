package com.dora.httpframework.listener;

import com.dora.httpframework.core.request.RequestHttpBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Describe ApplicationReadyEventListener
 * @Author dora 1.0.1
 **/
@Component
@Slf4j
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Application Ready ....");
        log.info("Start Perform ↓↓↓↓ ....");

        // 初始化模块
        create1();
        create2();
        //Bean相关测试
//        test1();

    }


    private void create1() {
        // Create RequestHttpBuilder
        if (RequestHttpBuilder.create() == null) {
            log.error("RequestHttpBuilder create failure!");
        } else {
            log.info("RequestHttpBuilder create success!");
        }
    }

    private void create2() {
        // Create

    }
}
