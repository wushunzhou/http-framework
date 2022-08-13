package com.dora.httpframework.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Dora
 * @Description: 上下文管理
 * @Date: 2022/2/23
 */
@Configuration
public class SpringContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String beanName) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }

    public static boolean contains(String name) {
        if (applicationContext == null) {
            return false;
        }
        return applicationContext.containsBean(name);
    }
}
