package com.dora.httpframework.support;

import com.dora.httpframework.core.base.Response;

/**
 * @Describe TODO:
 * @Author dora 1.0.1
 **/
public interface HttpPostProcessor extends PostProcessor {

    /**
     * http请求之前处理器
     * @param context
     */
    void requestsBeforePostProcessor(HttpContext context);

    /**
     * http请求之后处理器
     * @param context
     */
    void responseAfterPostProcessor(HttpContext context);


    /**
     * http请求后 对response对象进行各种处理后的处理器
     * 在{@link Response done()}内调用
     * @param context
     */
    void responseDonePostProcessor(HttpContext context);

}
