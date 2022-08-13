package com.dora.httpframework.utils;

import java.util.List;
import java.util.Map;

/**
 * @Describe 参数化工具
 * @Author dora 1.0.1
 **/
public class Parameterization {
    public static String wildcardMatcherString(String data) {
        return PatternUtil.replace(data);
    }

    public static void wildcardMatcherHeadersAndRequests(Map<String, Object> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        for (String key : data.keySet()) {
            if (data.get(key) instanceof String) {
                data.put(key, PatternUtil.replace((String) data.get(key)));
            }
        }
    }

    public static void wildcardMatcherValidate(Map<String, List<Object>> validateMatcher) {
        if (validateMatcher == null || validateMatcher.size() == 0) {
            return;
        }
        for (String key : validateMatcher.keySet()) {
            List<Object> lists = validateMatcher.get(key);
            if (lists != null && lists.size() > 0) {
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i) instanceof Map) {
                        Map<String, Object> map = (Map<String, Object>) lists.get(i);
                        for (String mapKey : map.keySet()) {
                            if (map.get(mapKey) instanceof String) {
                                map.put(mapKey, PatternUtil.replace((String) map.get(mapKey)));
                            }
                        }
                    }

                    if (lists.get(i) instanceof String) {
                        String arg = PatternUtil.replace((String) lists.get(i));
                        lists.set(i, arg);
                    }
                }
            }
        }
    }
}
