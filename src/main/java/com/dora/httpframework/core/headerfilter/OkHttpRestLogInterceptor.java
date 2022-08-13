package com.dora.httpframework.core.headerfilter;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * @Describe 请求响应打印
 * @Author dora 1.0.1
 **/
@Slf4j
public class OkHttpRestLogInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        //打印request报文
        Request request = chain.request();
        RequestBody requestBody = request.body();
        String reqBody = null;
        if (requestBody != null){
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType mediaType = requestBody.contentType();
            if (mediaType != null){
                charset = mediaType.charset(UTF8);
            }
            reqBody = buffer.readString(charset);
        }
        log.info("===========================start============================");
        log.info("url: [{}]",request.url());
        log.info("method: [{}]",request.method());
        log.info("requestHeaders: [{}]",request.headers());
        log.info("request: [{}]",reqBody);
        log.info("===========================done============================");

        //打印Response报文
        //先执行请求，才能获取报文
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        String respBody = null;
        if(responseBody != null) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType mediaType = responseBody.contentType();
            if (mediaType != null) {
                try {
                    charset = mediaType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    e.printStackTrace();
                }
            }
            respBody = buffer.clone().readString(charset);
        }
        log.info("===========================start============================");
        log.info("rCode: [{}]",response.code());
        log.info("response: [{}]", respBody);
        log.info("responseMsg: [{}]",response.message());
        log.info("===========================done============================");

        return response;
    }
}
