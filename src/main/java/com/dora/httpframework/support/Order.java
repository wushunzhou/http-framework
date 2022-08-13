package com.dora.httpframework.support;

/**
 * @Describe 接口排序
 * @Author dora 1.0.1
 **/
public interface Order {
    /**
     * 数字越小优先级越高
     * @return
     */
    int getOrder();
}
