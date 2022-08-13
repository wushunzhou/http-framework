package com.dora.httpframework.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Dora
 * @Description: Map工具类
 * @Date: 2021/12/11
 */
public class MapTools {
    private Map<String, String> map = new HashMap<>();

    private MapTools put(String k, String v) {
        map.put(k, v);
        return this;
    }

    public static Builder builder() {
        return new Builder ();
    }


    public static class Builder {
        private MapTools mapToolUtil = new MapTools();
        public Builder put(String k, String v) {
            mapToolUtil.put(k, v);
            return this;
        }

        public Map<String, String> build() {
            return mapToolUtil.map;
        }
    }
}
