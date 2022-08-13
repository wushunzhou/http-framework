package com.dora.httpframework.core.matches;

import com.dora.httpframework.parse.JsonPath;
import com.dora.httpframework.parse.PathParse;
import com.dora.httpframework.utils.SomeUtils;

import java.util.Map;

/**
 * @Describe path解析器
 * @Author dora 1.0.1
 **/
public class JsonPathValidateMatcher extends PathValidateMatcherAdapter {
    public JsonPathValidateMatcher(Map validate) {
        super(validate);
    }

    @Override
    public PathParse getParseHandler(String operand) {
        if (!SomeUtils.isJSONValid(operand)) {
            this.errorText = "返回值不为json:" + operand.getClass();
            return null;
        }
        return JsonPath.create(operand);
    }
}
