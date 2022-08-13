package com.dora.httpframework.core.matches;

import java.util.List;
import java.util.Map;

/**
 * @Describe 断言封装
 * @Author dora 1.0.1
 **/
public class Validate {
    /**
     * yaml jsonPath 断言
     * @param validate
     * @param <T>
     * @return
     */
    public static <T> org.hamcrest.Matcher<T> validateJson(Map<String, List<Object>> validate) {
        return new JsonPathValidateMatcher(validate);
    }

    /**
     * yaml xpath断言
     * @param validate
     * @param <T>
     * @return
     */
    public static <T> org.hamcrest.Matcher<T> validateXpath(Map<String, List<Object>> validate) {
        return new XPathValidateMatcher(validate);
    }

    public static <T> org.hamcrest.Matcher<T> validateEq(Object actual, Object expected) {
        return new EqValidateMatcher<>(actual, expected);
    }

}
