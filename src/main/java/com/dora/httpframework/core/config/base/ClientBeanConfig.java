package com.dora.httpframework.core.config.base;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Describe 扫描服务
 * @Author dora 1.0.1
 **/
@Configuration
public class ClientBeanConfig {
    //扫描包路径（默认全局）
    public static String[] scanning = {""};

    @ConditionalOnMissingBean(RegistryPostProcessor.class)
    @Bean
    public static RegistryPostProcessor ClientBeanDefinitionRegistryPostProcessor() {
        return new RegistryPostProcessor(scanning);
    }
}
