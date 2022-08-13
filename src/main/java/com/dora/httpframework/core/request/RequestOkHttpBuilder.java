package com.dora.httpframework.core.request;

import com.alibaba.fastjson.JSON;
import com.dora.httpframework.core.headerfilter.OkHttpHeadersInterceptor;
import com.dora.httpframework.core.headerfilter.OkHttpRestLogInterceptor;
import com.dora.httpframework.exception.DoraException;
import com.dora.httpframework.listener.OkHttpWebSockListener;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Dora
 * @Description: okHttp构建类
 * @Date: 2022/4/18
 */
@Slf4j
public class RequestOkHttpBuilder {

    private volatile static RequestOkHttpBuilder builder;

    /**
     * 最大连接时间
     */
    public final static int CONNECTION_TIMEOUT = 15 * 1000;
    /**
     * JSON格式
     */
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    /**
     * OkHTTP线程池最大空闲线程数
     */
    public final static int MAX_IDLE_CONNECTIONS = 100;
    /**
     * OkHTTP线程池空闲线程存活时间
     */
    public final static long KEEP_ALIVE_DURATION = 30L;

    public static String BASE64_PREFIX = "data:image/png;base64,";


    public static RequestOkHttpBuilder create() {
        if (builder == null) {
            synchronized (RequestOkHttpBuilder.class) {
                if (builder == null) {
                    builder = new RequestOkHttpBuilder();
                }
            }
        }
        return builder;
    }

    private RequestOkHttpBuilder() {
    }

    /**
     * client
     * 配置重试|日志拦截|Headers
     */
    public static OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION, TimeUnit.MINUTES))
            .pingInterval(10, TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
            // 新增 默认请求头
            .addInterceptor(new OkHttpHeadersInterceptor())
            // 新增 日志打印
            .addInterceptor(new OkHttpRestLogInterceptor())
            .build();

    /**
     * 设置请求头
     *
     * @param builder 请求实例
     * @param headers 请求头
     */
    private static void setHeaders(Request.Builder builder, Map<String, String> headers) {
        if (Objects.nonNull(headers) && headers.size() > 0) {
            headers.forEach((k, v) -> {
                if (Objects.nonNull(k) && Objects.nonNull(v)) {
                    builder.header(k, v);
                }
            });
        }
    }


    /**
     * @Description post 表单构建
     * @Data 2022/1/4
     * @Param [url, headers, parameters]
     **/
    public static Response post(String url, Map<String, String> headers, Map<String, String> parameters) throws IOException {
        Request.Builder builder = new Request.Builder();
        FormBody.Builder formParams = new FormBody.Builder();
        if (!CollectionUtils.isEmpty(parameters)) {
            parameters.forEach(formParams::add);
        }
        FormBody body = formParams.build();
        setHeaders(builder, headers);
        Request request = builder.url(url).post(body).build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        if (response.isSuccessful() && Objects.nonNull(response.body())) {
            return response;
        }
        return null;
    }

    /**
     * @Description post json构建
     * @Data 2022/1/4
     * @Param [url, headers, json]
     **/
    public static Response post(String url, Map<String, String> headers, String json) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request.Builder builder = new Request.Builder();
        setHeaders(builder, headers);
        Request request = builder.url(url).post(body).build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        if (response.isSuccessful() && Objects.nonNull(response.body())) {
            Response result = response;
            return result;
        }
        return null;
    }

    /**
     * get请求，无需转换对象
     * 且增加websocket监听
     *
     * @param url              链接
     * @param headers          null使用默认
     * @param OkHttpWsListener 设置ws监听器
     * @return 响应信息
     */
    public static WebSocket get(String url, Map<String, String> headers, WebSocketListener OkHttpWsListener) {
        Request.Builder builder = new Request.Builder();
        setHeaders(builder, headers);
        Request request = builder.url(url).build();
        return HTTP_CLIENT.newWebSocket(request, OkHttpWsListener);
    }

    /**
     * get请求，无需转换对象
     * 且增加websocket监听
     *
     * @param url     链接
     * @param headers null使用默认
     * @return 响应信息
     */
    public static WebSocket get(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        setHeaders(builder, headers);
        Request request = builder.url(url).build();
        return HTTP_CLIENT.newWebSocket(request, new OkHttpWebSockListener());
    }

    /**
     * 支持嵌套泛型的post请求。
     * <pre>
     *   Type type = new TypeToken<Results<User>>() {}.getType();
     * <pre/>
     *
     * @param url     链接
     * @param headers 请求头
     * @param json    请求json
     * @param type    嵌套泛型
     * @return 响应对象, 可进行强转。
     */
    public static <T> T post(String url, Map<String, String> headers, String json, Type type) throws IOException {
        Response result = post(url, headers, json);
        if (Objects.nonNull(result) && Objects.nonNull(type)) {
            return JSON.parseObject(result.toString(), type);
        }
        return null;
    }

    /**
     * 读取流，转换为Base64
     * 返回base64的照片
     */
    public static String postImg(String url, Map<String, String> headers, String json) {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request.Builder builder = new Request.Builder();
        setHeaders(builder, headers);
        Request request = builder.url(url).post(body).build();
        try {
            Response response = HTTP_CLIENT.newCall(request).execute();
            String contentType = null;
            if (response.body() != null && response.body().contentType() != null) {
                contentType = response.body().contentType().toString();
            }
            if ("image/png".equals(contentType)) {
                //读取流
                return BASE64_PREFIX + new String(Base64.getEncoder().encode(response.body().bytes()));
            }
        } catch (IOException e) {
            throw new DoraException("执行postImg请求，url: {} 失败!\n", url, e);
        }
        return null;
    }


//    @Test
//    private void RequestOkHttpBuilder() throws IOException {
//        RequestOkHttpBuilder builder = RequestOkHttpBuilder.create();
//        Map<String, String> headers = new OkHttpHeadersInterceptor().defHeaders();
//        headers.put("Content-Type", "application/json;charset=utf-8");
//        Map<String, String> parameters = new HashMap<String, String>();
//        Response response = builder.post("ws://XXXXXXXX/lthrift", headers, parameters);
//        System.out.println("响应：" + response.toString());
//    }
}
