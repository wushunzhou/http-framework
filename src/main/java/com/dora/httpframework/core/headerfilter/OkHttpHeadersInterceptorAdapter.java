package com.dora.httpframework.core.headerfilter;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

/**
 * @Describe OkHttp请求头适配器
 * @Author dora 1.0.1
 **/
public abstract class OkHttpHeadersInterceptorAdapter implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Map<String, String> defHeaders = defHeaders();
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder();
        defHeaders.forEach((k,v) ->{
            if(StringUtils.isBlank(original.headers().get(k.toLowerCase())) && !StringUtils.isBlank(v)){
                requestBuilder.addHeader(k,v);
            }
        });
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

    public abstract Map<String, String> defHeaders();
}
