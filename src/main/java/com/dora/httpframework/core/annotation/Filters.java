package com.dora.httpframework.core.annotation;

import io.restassured.filter.Filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: dora
 * @Date: 2019/8/23 11:03.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Filters {
    Class<? extends Filter>[] value();
}
