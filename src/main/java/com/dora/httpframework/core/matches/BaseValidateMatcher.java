package com.dora.httpframework.core.matches;

import com.dora.httpframework.utils.Parameterization;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.List;
import java.util.Map;

/**
 * @Describe TODO:
 * @Author dora 1.0.1
 **/
public abstract class BaseValidateMatcher<T> extends BaseMatcher<T> {
    protected Map<String, List<Object>> validateMatcher;

    protected String errorText = "";

    public BaseValidateMatcher(Map<String, List<Object>> validateMatcher) {
        this.validateMatcher = validateMatcher;
    }

    @Override
    public boolean matches(Object operand) {
        validateParameterization();
        return validate(operand);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(errorText);
    }

    protected abstract boolean validate(Object v);

    /**
     * 断言参数化处理
     */
    private void validateParameterization() {
        Parameterization.wildcardMatcherValidate(validateMatcher);
    }

}
