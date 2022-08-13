package com.dora.httpframework.enums;

/**
 * @Author: Dora
 * @Description: HTTP请求类型
 * @Date: 2021/12/13
 */
public enum HttpType {

    GET("get"),
    POST("post"),
    PUT("put"),
    DELETE("delete"),
    OPTIONS("options");

    private String type;

    HttpType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static HttpType get(String httpType) {
        switch (httpType) {
            case "get":
                return GET;
            case "post":
                return POST;
            case "put":
                return PUT;
            case "delete":
                return DELETE;
            case "options":
                return OPTIONS;
            default:
                    throw new IllegalArgumentException("No enum constant " + httpType);
        }
    }
}
