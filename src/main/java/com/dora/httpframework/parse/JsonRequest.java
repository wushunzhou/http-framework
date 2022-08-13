package com.dora.httpframework.parse;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @Describe JSON-String请求
 * @Author dora 1.0.1
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class JsonRequest extends BaseRequest {

    private String reqData;

    @Builder
    public JsonRequest(Map<String, Object> headers, String name, String description, String path, String method, String reqData) {
        super(headers,name,description,path,method);
        this.reqData = reqData;
    }

    @Override
    protected Object data() {
        return reqData;
    }
}
