package com.dora.httpframework.core.headerfilter;

import com.dora.httpframework.utils.MapTools;

import java.util.Map;

/**
 * @Describe Okhttp默认请求头
 * @Author dora 1.0.1
 **/
public class OkHttpHeadersInterceptor extends OkHttpHeadersInterceptorAdapter {
    @Override
    public Map<String, String> defHeaders() {
        return MapTools.builder()
                .put("Sec-WebSocket-Protocol", "push")
                .build();
    }

}
