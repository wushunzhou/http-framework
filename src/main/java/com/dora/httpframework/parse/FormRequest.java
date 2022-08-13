package com.dora.httpframework.parse;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @Describe
 * @Author dora 1.0.1
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class FormRequest extends BaseRequest {
    private Map<String, ?> reqData;

    @Builder
    public FormRequest(Map<String, Object> headers, String name, String description, String path, String method, Map<String, ?> reqData) {
        super(headers,name,description,path,method);
        this.reqData = reqData;
    }

    @Override
    protected Object data() {
        return reqData;
    }
}
