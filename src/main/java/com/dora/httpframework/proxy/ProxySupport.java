package com.dora.httpframework.proxy;

import com.dora.httpframework.core.annotation.Filters;
import com.dora.httpframework.core.annotation.HttpServer;
import com.dora.httpframework.core.request.RequestHttpBuilder;
import com.dora.httpframework.enums.HttpType;
import com.dora.httpframework.exception.DoraException;
import com.dora.httpframework.parse.BaseRequest;
import com.dora.httpframework.parse.FormRequest;
import com.dora.httpframework.parse.JsonRequest;
import com.dora.httpframework.support.HttpContext;
import com.dora.httpframework.support.HttpPostProcessor;
import com.dora.httpframework.support.PostProcessorHolder;
import com.dora.httpframework.utils.AssertUtils;
import com.dora.httpframework.utils.PatternUtil;
import com.dora.httpframework.utils.ResourcesUtil;
import com.google.common.collect.Lists;
import io.restassured.filter.Filter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.testng.collections.Maps;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @Describe 代理实现
 * @Author dora 1.0.1
 **/
public class ProxySupport {

    private final static String BASE_HTTP_PREFIX = "voderx.hosts.";

    private RequestHttpBuilder httpClient = RequestHttpBuilder.create();

    private List<Filter> filters = Lists.newArrayList();

    private Map<String, ?> params4Form = Maps.newHashMap();

    private String params4String = "";

    // 请求参数
    private List<BaseRequest> baseRequests;


    // 类传递
    private Class clazz;

    private Method method;


    public ProxySupport(Class clazz, Method method, Object[] args) {
        this.clazz = clazz;
        this.method = method;
        argsToCheck(args);
    }


    public final com.dora.httpframework.core.base.Response service() {
        baseUrlProcessor();
        filtersProcessor();
        return methodProcessor();
    }


    /**
     * 入参检查转化并保存
     */
    private void argsToCheck(Object[] args) {
        this.baseRequests = Lists.newArrayListWithCapacity(args.length);
        for (int i = 0; i < args.length; i++) {
            BaseRequest baseRequest = null;
            if (args[i] instanceof BaseRequest) {
                baseRequest = (BaseRequest) args[i];
            } else {
                throw new DoraException("类型转换错误,需要类型: BaseRequest.class, 实际类型:" + args[i].getClass().getName());
            }
            check(baseRequest);
            baseRequests.add(baseRequest);
        }
    }


    /**
     * method 核心执行
     * @return
     */
    private com.dora.httpframework.core.base.Response methodProcessor() {
        for (BaseRequest baseRequest : baseRequests) {
            HttpContext.HttpContextBuilder builder = HttpContext.builder();
            builder.baseRequest(baseRequest);
            PostProcessorHolder.getInstance().getPostProcessor(HttpPostProcessor.class)
                    .forEach(httpPostProcessor -> httpPostProcessor.requestsBeforePostProcessor(builder.build()));
            parameterizationProcessor();
            com.dora.httpframework.core.base.Response response = new com.dora.httpframework.core.base.Response(invokeHttpMethod(baseRequest), builder.build());
            PostProcessorHolder.getInstance().getPostProcessor(HttpPostProcessor.class)
                    .forEach(httpPostProcessor -> httpPostProcessor.responseAfterPostProcessor(builder.response(response).build()));
            return response;
        }
        throw new DoraException("Request execution exception");
    }

    /**
     * 根据入参类型-> 参数化 Map
     */
    private void parameterizationProcessor() {
        baseRequests.forEach(baseRequest -> {
            if (baseRequest.getRequests() instanceof Map) {
                params4Form = baseRequest.getRequests();
                // 处理 value 类型为 String[] 的进行强转
//                for (Map.Entry<String, ?> entry : params4Form.entrySet()) {
//                    if (entry.getValue() instanceof String[]){
//                        System.out.println(entry.getValue()); TODO:
//                    }
//                }
            }
            if (baseRequest.getRequests() instanceof String) {
                params4String = baseRequest.getRequests();
            }
        });
    }

    private Response invokeHttpMethod(BaseRequest baseRequest) {
        switch (HttpType.get(baseRequest.getMethod().toLowerCase())) {
            case GET:
                return doGet(baseRequest);
            case PUT:
                return doPut(baseRequest);
            case POST:
                return doPost(baseRequest);
            case DELETE:
                return doDelete(baseRequest);
            case OPTIONS:
                return doOptions(baseRequest);
            default:
                throw new DoraException("不支持的方法:" + baseRequest.getMethod());
        }
    }

    /**
     * baseUrl
     */
    private void baseUrlProcessor() {
        HttpServer httpServerAnnotation = (HttpServer) clazz.getAnnotation(HttpServer.class);
        if (!StringUtils.isBlank(httpServerAnnotation.baseUrl())) {
            String url = httpServerAnnotation.baseUrl();
            List<String> patterns = PatternUtil.getPatterns(url);
            if (patterns.size() > 0) {
                for (String p : patterns) {
                    String v = ResourcesUtil.getProp(new StringBuffer(BASE_HTTP_PREFIX).append(p).toString());
                    AssertUtils.notNull(v, PatternUtil.createKey(p) + "配置文件中未找到域名配置");
                    url = url.replace(PatternUtil.createKey(p), v);
                }
            }
            httpClient.setBaseUrl(url);
        }
    }

    /**
     * filters
     */
    private void filtersProcessor() {
        Filters classFilters = (Filters) clazz.getAnnotation(Filters.class);
        Filters methodFilters = method.getAnnotation(Filters.class);
        doFilter(classFilters);
        doFilter(methodFilters);
    }

    private void doFilter(Filters filter) {
        if (filter == null) {
            return;
        }
        for (int i = 0; i < filter.value().length; i++) {
            try {
                filters.add(filter.value()[i].newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private Response doGet(BaseRequest request) {
        return httpClient.get(request.getHeaders(), params4Form, request.getPath(), filters.toArray(new Filter[filters.size()]));
    }

    private Response doPost(BaseRequest request) {
        if (request instanceof JsonRequest) {
            return httpClient.post(request.getHeaders(), params4String, ContentType.JSON, request.getPath(), filters.toArray(new Filter[filters.size()]));
        }
        if (request instanceof FormRequest) {
            return httpClient.post(request.getHeaders(), params4Form, request.getPath(), filters.toArray(new Filter[filters.size()]));
        }
        throw new DoraException("暂不支持request data type: " + request.getClass());
    }

    private Response doPut(BaseRequest request) {
        if (request instanceof JsonRequest) {
            return httpClient.put(request.getHeaders(), params4String, ContentType.JSON, request.getPath(), filters.toArray(new Filter[filters.size()]));
        }
        if (request instanceof FormRequest) {
            return httpClient.put(request.getHeaders(), params4Form, request.getPath(), filters.toArray(new Filter[filters.size()]));
        }
        throw new DoraException("暂不支持request data type: " + request.getClass());
    }

    private Response doDelete(BaseRequest request) {
        return httpClient.delete(request.getHeaders(), params4Form, request.getPath(), filters.toArray(new Filter[filters.size()]));
    }

    private Response doOptions(BaseRequest request) {
        return httpClient.options(request.getHeaders(),request.getPath());
    }


    private void check(BaseRequest request) {
        AssertUtils.notNull(request, "http请求model为null");
        AssertUtils.notNull(request.getName(), "http请求Model Name为null");
        AssertUtils.notNull(request.getMethod(), "http请求类型Method为null, request:" + request.getName());
        AssertUtils.notNull(request.getPath(), "http请求Model path为null, request:" + request.getName());
    }
}
