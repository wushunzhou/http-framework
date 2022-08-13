package com.dora.httpframework.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @Describe JsonObj工具类
 * @Author dora 1.0.1
 **/
public class JSONObjTools {

    private JSONObject json = new JSONObject();

    private JSONObjTools put(String k, Object v) {
        json.put(k, v);
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private JSONObjTools jsonObjectToolUtil = new JSONObjTools();
        public Builder put(String k, Object v) {
            jsonObjectToolUtil.put(k, v);
            return this;
        }

        public JSONObject build() {
            return jsonObjectToolUtil.json;
        }
    }
}
