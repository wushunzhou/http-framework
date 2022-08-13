package com.dora.httpframework.core.matches;

import com.dora.httpframework.exception.DoraException;
import com.dora.httpframework.parse.PathParse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @Describe 断言适配器
 * @Author dora 1.0.1
 **/
public abstract class PathValidateMatcherAdapter<T> extends BaseValidateMatcher<T> {
    public PathValidateMatcherAdapter(Map<String, List<Object>> validateMatcher) {
        super(validateMatcher);
    }

    @Override
    protected boolean validate(Object operand) {

        if (!(operand instanceof String)) {
            this.errorText = "返回值类型匹配出错,需要类型: String,实际类型: " + operand.getClass();
            return false;
        }

        if (validateMatcher == null || validateMatcher.size() == 0) {
            return true;
        }

        PathParse pathParse = getParseHandler(String.valueOf(operand));

        if (pathParse == null) {
            return false;
        }

        ValidateByPathHandlers validateByJsonHandlers = new ValidateByPathHandlers(pathParse);
        Method[] declaredMethods = validateByJsonHandlers.getClass().getDeclaredMethods();
        boolean isMethodExist = false;

        for(String k: validateMatcher.keySet()) {
            for(Method method : declaredMethods) {
                if (k.equals(method.getName())) {
                    isMethodExist = true;
                    for(Object obj : validateMatcher.get(k)) {
                        try {
                            method.invoke(validateByJsonHandlers, obj);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            if(e.getTargetException() instanceof DoraException) {
                                this.errorText = e.getTargetException().getMessage();
                                return false;
                            }
                            throw new DoraException("%s类, 方法:%s, 异常: %s", validateByJsonHandlers.getClass().getName(), k, e.getTargetException());
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            throw new DoraException("yaml validate解析错误,请检查: " + validateMatcher.get(k));
                        }
                    }
                }
            }
            if (!isMethodExist) {
                throw new DoraException("断言方法不存在: " + k);
            }
        }
        return true;
    }

    /** 获取path解析器*/
    public abstract PathParse getParseHandler(String operand);
    
}
