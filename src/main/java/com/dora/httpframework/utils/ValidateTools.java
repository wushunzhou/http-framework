package com.dora.httpframework.utils;

import com.dora.httpframework.enums.MatchesEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Describe validateMap
 * @Author dora 1.0.1
 **/
public class ValidateTools {
    private Map<MatchesEnum, List<Object>> map = new HashMap<>();

    private ValidateTools put(MatchesEnum type, List<Object> data) {
        map.put(type, data);
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private ValidateTools validateTools = new ValidateTools();
        public Builder put(MatchesEnum type, List<Object> data) {
            validateTools.put(type, data);
            return this;
        }

        public Map<MatchesEnum, List<Object>> build() {
            return validateTools.map;
        }
    }
}
