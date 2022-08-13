package com.dora.httpframework.proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Describe TODO:
 * @Author dora 1.0.1
 **/
public class ProxyFactoryBean implements FactoryBean<Object>, InitializingBean {
    private Class<?> clazz;

    private Object proxy;


    @Override
    public Object getObject() throws Exception {
        return proxy;
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        this.proxy = ProxyApi.getProxyInstance(clazz);
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
