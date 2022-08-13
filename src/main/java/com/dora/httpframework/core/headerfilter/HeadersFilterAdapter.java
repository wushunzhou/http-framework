package com.dora.httpframework.core.headerfilter;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @Author: Dora
 * @Description: Headers适配器
 * @Date: 2021/12/14
 */
public abstract class HeadersFilterAdapter implements Filter {
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Map<String, String> defHeaders = defHeaders();

        defHeaders.forEach((k, v) -> {
            if (StringUtils.isBlank(requestSpec.getHeaders().getValue(k.toLowerCase())) && !StringUtils.isBlank(v)) {
                requestSpec.header(k.toLowerCase(), v);
            }
        });
        return ctx.next(requestSpec, responseSpec);
    }

    public abstract Map<String, String> defHeaders();
}
