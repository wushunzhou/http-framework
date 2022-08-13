package com.dora.httpframework.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Dora
 * @Description: Json提取工具
 * @Date: 2021/12/14
 */
public class JSONSerializer {

    /**深拷贝*/
    public static <T> T copy(T t, Class<T> clazz) {
        return unSerialize(serializeExistNull(t), clazz);
    }

    /**
     * 过滤value为null的key
     * @param t
     * @return 无格式
     */
    public static <T> String serialize(T t) {
        if (t == null) {
            return "";
        } else {
            return JSON.toJSONString(t);
        }
    }

    /**
     * 过滤value为null的key
     * @param t
     * @return Format格式化
     */
    public static <T> String serializeFormat(T t) {
        if (t == null) {
            return "";
        } else {
            // TODO: SerializerFeature.DisableCircularReferenceDetect,,SerializerFeature.PrettyFormat
            return JSON.toJSONString(t,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteDateUseDateFormat,SerializerFeature.PrettyFormat);
        }
    }

    public static <T> Object serializeObj(T t) {
        if (t == null) {
            return "";
        } else {
            return JSONObject.toJSON(t);
        }
    }


    // 反序列化
    public static <T> T unSerialize(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        } else {
            return JSON.parseObject(json, clazz);
        }
    }

    // 反序列化
    public static <T> T unSerialize(byte[] json, Class<T> clazz) {
        if (json.length == 0) {
            return null;
        } else {
            return JSON.parseObject(json, clazz);
        }
    }

    /**
     * 不过滤value为null的key
     * @param t
     * @return
     */
    public static <T> String serializeExistNull(T t) {
        if (t == null) {
            return "";
        } else {
            return JSON.toJSONString(t, SerializerFeature.WriteMapNullValue);
        }
    }

    /**
     * entity -> jsonobject
     * @param t
     * @param <T>
     * @return
     */
    public static <T> JSONObject serializeToJsonobject(T t) {
        if (t instanceof JSONObject) {
            return (JSONObject) t;
        }

        return (JSONObject) JSON.toJSON(t);
    }
}
