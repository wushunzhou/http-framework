package com.dora.httpframework.core.client;

import com.dora.httpframework.core.base.Response;
import com.dora.httpframework.parse.BaseRequest;

import java.util.concurrent.TimeUnit;

/**
 * @Describe TODO:
 * @Author dora 1.0.1
 **/
public interface BaseHttpClient {

    /**
     * 调用接口前等待时间
     * @param unit
     * @param interval
     * @return
     */
    <T extends BaseHttpClient> T wait(TimeUnit unit, long interval);

    /**
     * 调用接口前设置一个作用域在该请求级别的缓存
     * @param k
     * @param v
     * @return
     */
    <T extends BaseHttpClient> T saveAsk(String k, Object v);

    /**
     * 调用接口前设置一个作用域在该测试方法的缓存
     * @param k
     * @param v
     * @return
     */
    <T extends BaseHttpClient> T saveMethod(String k, Object v);

    /**
     * 调用接口前设置一个作用域在该线程的缓存
     * @param k
     * @param v
     * @return
     */
    <T extends BaseHttpClient> T saveThread(String k, Object v);

    /**
     * 调用接口前设置一个作用域全局的缓存
     * @param k
     * @param v
     * @return
     */
    <T extends BaseHttpClient> T saveGlobal(String k, Object v);


    /**
     * HTTP请求发起
     * @param baseRequest
     * @return
     */
    Response doHttp(BaseRequest baseRequest);


}
