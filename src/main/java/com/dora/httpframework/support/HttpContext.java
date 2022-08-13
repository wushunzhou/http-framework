package com.dora.httpframework.support;

import com.dora.httpframework.core.base.Response;
import com.dora.httpframework.parse.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Describe HTTP上下文构造
 * @Author dora 1.0.1
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpContext {

    /** 请求 */
    private BaseRequest baseRequest;

    /**响应 */
    private Response response;

    /** response操作中的throw Throwable*/
    private Throwable throwable;
}
