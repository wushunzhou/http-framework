package com.dora.httpframework.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Dora
 * @Description: yml文件读取工具
 * 逻辑原理：加载yml文件，遍历相对应的Key，缓存成Map
 * @Date: 2022/2/23
 */
@Slf4j
public class YmlFileMapTool {
    // 默认读取yml文件
    private static String bootstrap_file = "application-qa.yml";
    private static Map<String, String> result = new HashMap<>();

    /**
     * 根据文件名获取yml的文件内容
     *
     * @return
     */
    public Map<String, String> getYmlByFileName(String file) {
        result = new HashMap<>();
        if (file == null)
            file = bootstrap_file;
        InputStream in = YmlFileMapTool.class.getClassLoader().getResourceAsStream(file);
        if (in == null) {
            log.error(file + "This is no ！\n");
            return result;
        }
        Yaml props = new Yaml();
        Object obj = props.loadAs(in, Map.class);
        Map<String, Object> param = (Map<String, Object>) obj;
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();

            if (val instanceof Map) {
                forEachYaml(key, (Map<String, Object>) val);
            } else {
                result.put(key, val.toString());
            }
        }
        return result;
    }

    /**
     * 根据key获取值
     * @param key
     * @return
     */
    public String getValue(String file, String key) {
        Map<String, String> map = getYmlByFileName(file);
        if (map == null) return null;
        return map.get(key);
    }

    /**
     * 遍历yml文件，获取map集合
     *
     * @param key_str
     * @param obj
     * @return
     */
    public static Map<String, String> forEachYaml(String key_str, Map<String, Object> obj) {
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            String str_new = "";
            if (StringUtils.isEmpty(key_str)) {
                str_new = key_str + "." + key;
            } else {
                str_new = key;
            }
            if (val instanceof Map) {
                forEachYaml(str_new, (Map<String, Object>) val);
            } else {
                result.put(str_new, val.toString());
            }
        }
        return result;
    }

}
