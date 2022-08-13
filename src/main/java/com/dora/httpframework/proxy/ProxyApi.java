package com.dora.httpframework.proxy;

import java.lang.reflect.Proxy;

/**
 * @Describe 动态代理
 * @Author dora 1.0.1
 **/
public class ProxyApi {

    //debug模式出现Ex属于正常
    public static <T> T getProxyInstance(Class<T> clazz) {

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
                (proxy, method, args) -> {
                    if(method.getName().equals(ProxyMethodName.doHttp.name())) {
                        ProxySupport execute = new ProxySupport(clazz, method, args);
                        return execute.service();
                    } else {
                        ProxySupportBefore before = new ProxySupportBefore(clazz, method, args);
                        return before.handle();
                    }
                });
    }

    public enum ProxyMethodName{
        doHttp,
    }
}
