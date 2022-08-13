package com.dora.httpframework.core.base;

import com.dora.httpframework.parse.BaseRequest;

import java.util.Map;

/**
 * @Describe ExtractResponse
 * @Author dora 1.0.1
 **/
public interface ExtractResponseOptions {
    /**
     * 获取返回头
     *
     * @param header
     * @return
     */
    String getHeader(String header);

    /**
     * 获取返回值为json的body
     *
     * @return
     */
    String getJsonBody();

    /**
     * 获取返回值为xml的body
     *
     * @return
     */
    Object getXmlBody();

    /**
     * 获取返回值为html的body
     *
     * @return
     */
    Object getHtmlBody();


    /**
     * 获取返回值为的T的body
     *
     * @return
     */
    <T> T getAsTBody(Class<T> cls);

    /**
     * 获取所有返回头
     *
     * @return
     */
    Map<String, String> getHeaders();

    /**
     * 获取返回状态code
     *
     * @return
     */
    int getStatusCode();

    /**
     * 获取sessionId
     *
     * @return
     */
    String getSessionId();

    /**
     * 获取cookie
     *
     * @param name
     * @return
     */
    String getCookie(String name);

    /**
     * 获取所有cookie
     *
     * @return
     */
    Map<String, String> cookies();

    /**
     * 获取path位置值
     * See Also:
     * jsonPath(), xmlPath()
     * @param path
     * @return
     */
    Object getPath(String path);


    /**
     * 获取请求信息
     * @return
     */
    BaseRequest getRequests();

}
