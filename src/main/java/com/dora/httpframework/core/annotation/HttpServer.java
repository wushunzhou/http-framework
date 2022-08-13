package com.dora.httpframework.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: dora
 * @Date: 2019/8/21 17:17.
 *
 * 接口类标注,必填,否则无法扫描到容器,无法注入
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface HttpServer {
    /**
     * 非必填<br>
     * @return
     */
    String baseUrl() default "";
}
