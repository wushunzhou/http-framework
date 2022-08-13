package com.dora.httpframework.core.matches;

import com.dora.httpframework.exception.DoraException;
import com.dora.httpframework.parse.PathParse;
import com.dora.httpframework.parse.Xpath;

import java.util.Map;

/**
 * @Describe TODO:
 * @Author dora 1.0.1
 **/
public class XPathValidateMatcher extends PathValidateMatcherAdapter{
    public XPathValidateMatcher(Map validate) {
        super(validate);
    }

    @Override
    public PathParse getParseHandler(String operand) {
        try {
            Xpath xpath = Xpath.of(operand);
            return xpath;
        } catch (DoraException e) {
            e.printStackTrace();
            this.errorText = "返回值xml解析错误: " + operand;
        }
        return null;
    }
}
