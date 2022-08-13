package com.dora.httpframework.utils;

import org.springframework.core.env.Environment;

/**
 * @Author: Dora
 * @Description: Env管理工具
 * @Date: 2022/4/24
 */
public class ResourcesUtil {
    private static Environment environment = SpringContext.getBean(Environment.class);

    public static String getProp(String key) {
        return environment.getProperty(key);
    }

    public static <T>T getPropClass(String key,Class<T> targetType) {
        return environment.getProperty(key,targetType);
    }

    public static Boolean getPropBoolean(String key) {
        return Boolean.valueOf(environment.getProperty(key));
    }

}
