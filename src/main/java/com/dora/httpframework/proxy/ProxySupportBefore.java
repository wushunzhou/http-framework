package com.dora.httpframework.proxy;

import com.dora.httpframework.core.base.Response;
import com.dora.httpframework.core.client.BaseHttpClient;
import com.dora.httpframework.parse.BaseRequest;
import com.dora.httpframework.utils.ContextDataStorage;
import com.dora.httpframework.utils.SpringContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @Describe 代理支持
 * @Author dora 1.0.1
 **/
public class ProxySupportBefore<T extends BaseHttpClient> implements BaseHttpClient {

    private Class<T> clazz;

    private Method method;

    private Object[] args;

    public ProxySupportBefore(Class<T> clazz, Method method, Object[] args) {
        this.clazz = clazz;
        this.method = method;
        this.args = args;
    }

    public final T handle() {
        try {
            method.invoke(this, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return SpringContext.getBean(clazz);
    }


    @Override
    public BaseHttpClient wait(TimeUnit unit, long interval) {
        try {
            unit.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public BaseHttpClient saveAsk(String k, Object v) {
        ContextDataStorage.getInstance().setAskAttribute(k, v);
        return this;
    }

    @Override
    public BaseHttpClient saveMethod(String k, Object v) {
        ContextDataStorage.getInstance().setMethodAttribute(k, v);
        return this;
    }

    @Override
    public BaseHttpClient saveThread(String k, Object v) {
        ContextDataStorage.getInstance().setThreadAttribute(k, v);
        return this;
    }

    @Override
    public BaseHttpClient saveGlobal(String k, Object v) {
        ContextDataStorage.getInstance().setAttribute(k, v);
        return this;
    }

    @Override
    public Response doHttp(BaseRequest baseRequest) {
        return null;
    }


}
