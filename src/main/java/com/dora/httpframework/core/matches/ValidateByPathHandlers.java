package com.dora.httpframework.core.matches;

import com.dora.httpframework.enums.MatchesEnum;
import com.dora.httpframework.exception.DoraException;
import com.dora.httpframework.parse.PathParse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Describe path类型断言处理, 适用于Xpath, Jpath
 * @Author dora 1.0.1
 **/
public class ValidateByPathHandlers {
    private PathParse path;

    public ValidateByPathHandlers(PathParse pathParse) {
        this.path = pathParse;
    }

    public void eq(Map<String, Object> map) {
        for (String tempKey : map.keySet()) {
            //jsonpath解析double类型会转换成BigDecimal类型
            boolean isReturn = path.get(tempKey) instanceof BigDecimal ?
                    ((BigDecimal) path.get(tempKey)).doubleValue() == new BigDecimal(String.valueOf(map.get(tempKey))).doubleValue() :
                    ValidateUtils.eq(path.get(tempKey), map.get(tempKey));
            if (!isReturn) {
                throw new DoraException(prettyErrorMsg(map.get(tempKey), path.get(tempKey), tempKey, MatchesEnum.EQ,"两值不匹配"));
            }
        }
    }

    public void plugin(Map<String, Object> plugins) {
        try {
            boolean isReturn = ValidateUtils.plugin(plugins);
            if (!isReturn) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("plugin执行返回false,执行方法: ").append(plugins.get(ValidateUtils.METHOD_NAME_KEY)).append(",")
                        .append("入参:").append(plugins.get(ValidateUtils.METHOD_ARGS_KEY));
                throw new DoraException(prettyErrorMsg(null, null, null, MatchesEnum.PLUGIN, stringBuffer.toString()));
            }
        } catch (Exception e) {
            if (e instanceof DoraException) {
                throw new DoraException(e.getMessage());
            } else {
                e.printStackTrace();
                throw new DoraException("plugin执行报错,error: " + e.getMessage());
            }
        }
    }

    public void isNull(String text) {
        boolean isReturn = ValidateUtils.isNull(path.get(text));
        if (!isReturn) {
            throw new DoraException(prettyErrorMsg(null, path.get(text), text, MatchesEnum.NULL, "该值不为null"));
        }
    }

    public void notNull(String text) {
        boolean isReturn = ValidateUtils.notNull(path.get(text));
        if (!isReturn) {
            throw new DoraException(prettyErrorMsg(null, null, text, MatchesEnum.NOTNULL, "该值为null"));
        }
    }

    public void gt(Map<String, Object> map) {
        for (String tempKey : map.keySet()) {
            Object value = map.get(tempKey);
            Object key = path.get(tempKey);
            BigDecimal operand, expected;
            try{
                operand = new BigDecimal(String.valueOf(key));
                expected = new BigDecimal(String.valueOf(value));
            } catch (NumberFormatException e) {
                throw new DoraException("大于小于比较值错误:" + tempKey);
            }
            boolean isReturn = ValidateUtils.gt(operand, expected);
            if (!isReturn) {
                throw new DoraException(prettyErrorMsg(map.get(tempKey), path.get(tempKey), tempKey, MatchesEnum.GT, "实际值不大于预期值"));
            }
        }
    }

    public void lt(Map<String, Object> map) {
        for (String tempKey : map.keySet()) {
            Object value = map.get(tempKey);
            Object key = path.get(tempKey);
            BigDecimal operand, expected;
            try{
                operand = new BigDecimal(String.valueOf(key));
                expected = new BigDecimal(String.valueOf(value));
            } catch (NumberFormatException e) {
                throw new DoraException("大于小于比较值错误:" + tempKey);
            }
            boolean isReturn = ValidateUtils.lt(operand, expected);
            if (!isReturn) {
                throw new DoraException(prettyErrorMsg(map.get(tempKey), path.get(tempKey), tempKey, MatchesEnum.LT, "实际值不小于预期值"));
            }
        }
    }

    public void contains(Map<String, Object> map) {
        for (String tempKey : map.keySet()) {
            if (!(path.get(tempKey) instanceof List)) {
                throw new DoraException("contains包含类型只能是List,报错key:" + tempKey);
            }
            List key = (List) path.get(tempKey);
            boolean isReturn = ValidateUtils.contains(key, map.get(tempKey));
            if (!isReturn) {
                throw new DoraException(prettyErrorMsg(map.get(tempKey), key, tempKey, MatchesEnum.CONTAINS, "实际数组不包含预期Key"));
            }
        }
    }

    public void hasKey(Map<String, Object> map) {
        for (String tempKey : map.keySet()) {
            if (!(path.get(tempKey) instanceof Map)) {
                throw new DoraException("hasKey断言类型必须为MAP,报错key:" + tempKey);
            }
            Map<Object, Object> key = (Map) path.get(tempKey);
            boolean isReturn = ValidateUtils.hasKey(key, map.get(tempKey));
            if (!isReturn) {
                throw new DoraException(prettyErrorMsg(map.get(tempKey), key, tempKey, MatchesEnum.HASKEY, "实际Map不包含预期Key"));
            }
        }
    }

    public void hasValue(Map<String, Object> map) {
        for (String tempKey : map.keySet()) {
            if (!(path.get(tempKey) instanceof Map)) {
                throw new DoraException("hasKey断言类型必须为MAP,报错key:" + tempKey);
            }
            Map<Object, Object> key = (Map) path.get(tempKey);
            boolean isReturn = ValidateUtils.hasValue(key, map.get(tempKey));
            if (!isReturn) {
                throw new DoraException(prettyErrorMsg(map.get(tempKey), key, tempKey, MatchesEnum.HASVAULE, "实际Map不包含预期Value"));
            }
        }
    }

    public void len(Map<String, Object> map) {
        for (String tempKey : map.keySet()) {
            boolean isReturn = ValidateUtils.len(path.get(tempKey), Integer.parseInt(String.valueOf(map.get(tempKey))));
            if (!isReturn) {
                throw new DoraException(prettyErrorMsg(map.get(tempKey), path.get(tempKey), tempKey, MatchesEnum.LEN, "实际长度与预期长度不匹配"));
            }
        }
    }

    public static String prettyErrorMsg(Object expected, Object actual, String assertKey, MatchesEnum condition, String error) {
        StringBuffer pretty = new StringBuffer();
        pretty.append(expected).append("\n")
                .append("ActualValue: ").append(actual).append("\n")
                .append("AssertKey: ").append(assertKey).append("\n")
                .append("Condition: ").append(condition.getType()).append("\n")
                .append("ErrorText: ").append(error);
        return pretty.toString();
    }
}
