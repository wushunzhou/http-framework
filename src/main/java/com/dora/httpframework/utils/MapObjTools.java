package com.dora.httpframework.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Describe Map泛型工具类
 * @Author dora 1.0.1
 **/
public class MapObjTools<T> {
    private Map<String, T> map = new HashMap<>();

    private MapObjTools put(String k, T v) {
        map.put(k, v);
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private MapObjTools mapObjToolUtil = new MapObjTools();
        public <T> Builder put(String k, T v) {
            mapObjToolUtil.put(k, v);
            return this;
        }

        public <T> Map<String, T> build() {
            return mapObjToolUtil.map;
        }
    }
}
