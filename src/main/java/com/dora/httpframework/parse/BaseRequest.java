package com.dora.httpframework.parse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Describe 基础请求数据结构
 * @Author dora 1.0.1
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class BaseRequest {
    protected Map<String, Object> headers; //可空
    protected String name; //API名称
    protected String description; //API描述 可空
    protected String path; //API请求路径
    protected String method; //请求类型

    public final <T> T getRequests() {
        return data();
    }

    protected abstract <T> T data();
}
