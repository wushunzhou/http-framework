package com.dora.httpframework.parse;

import com.alibaba.fastjson.*;

/**
 * @Describe json处理
 * @Author dora 1.0.1
 **/
public class JsonPath implements PathParse {
    private JSON json;

    private JsonPath() {
    }

    private JsonPath(String json) {
        try {
            this.json = JSONObject.parseObject(json);
        }catch (JSONException e) {
            this.json = JSONArray.parseArray(json);
        }
    }

    public static JsonPath create(String json){
        return new JsonPath(json);
    }

    @Override
    public Object get(String path) {
        return JSONPath.eval(json, path);
    }

    @Override
    public boolean isExist(String key) {
        try {
            return JSONPath.contains(json, key);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 是否包含，path中是否存在对象
     * @param path
     * @return
     */
    public boolean contains(String path) {
        boolean exits = isExist(path);
        if (!exits) {
            return exits;
        }
        return JSONPath.contains(json, path);
    }


    /**
     * 是否包含，path中是否存在对象value
     * @param path
     * @param value
     * @return
     */
    public boolean containsValue(String path, Object value) {
        boolean exits = isExist(path);
        if (!exits) {
            return exits;
        }
        return JSONPath.containsValue(json, path, value);
    }

    @Override
    public int size(String path) {
        return JSONPath.size(json, path);
    }

    public JsonPath set(String path, Object v) {
        JSONPath.set(json, path, v);
        return this;
    }

    public JsonPath arrayAdd(String path, Object...values) {
        JSONPath.arrayAdd(json, path, values);
        return this;
    }

    public String getString(){
        return json.toJSONString();
    }

}
