package com.dora.httpframework.core.headerfilter;

import com.dora.httpframework.utils.JSONSerializer;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Describe 请求响应打印
 * @Author dora 1.0.1
 **/
@Slf4j
public class BaseRestLogFilter implements Filter {
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);

        String url = requestSpec.getURI();
        String method = requestSpec.getMethod();
        Headers headers = requestSpec.getHeaders();

        Map reqFormParams =  requestSpec.getFormParams();
        String requestsStr = reqFormParams != null && reqFormParams.size() > 0 ? JSONSerializer.serialize(reqFormParams)
                : JSONSerializer.serialize(requestSpec.getBody());
        String responseStr = response.asString();
        log.info("===========================start============================");
        log.info("url: [{}]", url);
        log.info("method: [{}]", method);
        log.info("requestHeaders: {}", headers.asList());
        log.info("request: [{}]", requestsStr);
        log.info("response: [{}]", responseStr);
        log.info("===========================done============================");
        return response;
    }
}
