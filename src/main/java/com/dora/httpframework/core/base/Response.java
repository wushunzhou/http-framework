package com.dora.httpframework.core.base;

import com.dora.httpframework.core.matches.Validate;
import com.dora.httpframework.core.matches.ValidateUtils;
import com.dora.httpframework.enums.MatchesEnum;
import com.dora.httpframework.exception.AssertionException;
import com.dora.httpframework.parse.BaseRequest;
import com.dora.httpframework.support.HttpContext;
import com.dora.httpframework.utils.AssertUtils;
import com.dora.httpframework.utils.ContextDataStorage;
import com.dora.httpframework.utils.JSONSerializer;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.testng.collections.Maps;

import java.util.*;

/**
 * @Describe Response
 * @Author dora 1.0.1
 **/
@Slf4j
public class Response implements ValidateResponseOptions<Response>, SaveDataResponseOptions<Response>, ExtractResponseOptions {
    private io.restassured.response.Response assuredResponse;

    private RuntimeException ex;

    private BaseRequest request;
    private ContentType contentType;

    private HttpContext context;

    private Response() {
    }

    /**
     * Syntactic sugar =>  Response.then().XXX
     */
    public Response then() {
        return this;
    }

    /**
     * Open the native => Response 不建议使用
     **/
    private io.restassured.response.Response getResponse() {
        return assuredResponse;
    }

    public Response(io.restassured.response.Response assuredResponse, HttpContext context) {
        this.assuredResponse = assuredResponse;
        this.context = context;
        this.context.setResponse(this);
        this.request = context.getBaseRequest();
        this.contentType = ContentType.fromContentType(assuredResponse.getContentType());
    }


    public Response statusCode(int code) {
        if (exitsEx()) {
            return this;
        }
        try {
            assuredResponse.then().statusCode(code);
        } catch (Throwable ex) {
            this.ex = new AssertionException(ex.getMessage(), ex.getCause());
        }
        return this;
    }

    @Override
    public String getHeader(String header) {
        return assuredResponse.getHeader(header);
    }

    @Override
    public String getJsonBody() {
        Object o = assuredResponse.jsonPath().get();
        return JSONSerializer.serialize(o);
    }

    @Override
    public Object getXmlBody() {
        return assuredResponse.xmlPath().get();
    }

    @Override
    public Object getHtmlBody() {
        return assuredResponse.xmlPath().get();
    }

    //转化
    @Override
    public <T> T getAsTBody(Class<T> cls) {
        return assuredResponse.getBody().as(cls);
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = Maps.newHashMap();
        // 迭代器
        Iterator<Header> iterator = assuredResponse.getHeaders().iterator();
        while (iterator.hasNext()) {
            Header next = iterator.next();
            headers.put(next.getName(), next.getValue());
        }
        return headers;
    }

    @Override
    public int getStatusCode() {
        return assuredResponse.getStatusCode();
    }

    @Override
    public String getSessionId() {
        return assuredResponse.getSessionId();
    }

    @Override
    public String getCookie(String name) {
        return assuredResponse.getCookie(name);
    }

    @Override
    public Map<String, String> cookies() {
        return assuredResponse.getCookies();
    }

    @Override
    public Object getPath(String path) {
        return assuredResponse.path(path);
    }

    @Override
    public BaseRequest getRequests() {
        return this.request;
    }

    @Override
    public Response saveGlobal(String key, String path) {
        if (exitsEx()) {
            return this;
        }
        ContextDataStorage.getInstance().setAttribute(key, getPathValue(path));
        return this;
    }

    @Override
    public Response saveMethod(String key, String path) {
        if (exitsEx()) {
            return this;
        }
        ContextDataStorage.getInstance().setMethodAttribute(key, getPathValue(path));
        return this;
    }

    @Override
    public Response saveClass(String key, String path) {
        if (exitsEx()) {
            return this;
        }
        ContextDataStorage.getInstance().setClassAttribute(key, getPathValue(path));
        return this;
    }

    @Override
    public Response saveThread(String key, String path) {
        if (exitsEx()) {
            return this;
        }
        ContextDataStorage.getInstance().setThreadAttribute(key, getPathValue(path));/**/
        return this;
    }

    @Override
    public Response validateByPath(String path, Matcher matcher) {
        if (exitsEx()) {
            return this;
        }
        try {
            assuredResponse.then().body(path, matcher);
        } catch (Throwable e) {
            this.ex = new AssertionException(ex.getMessage(), ex.getCause());
//            throw ex;
        }
        return this;
    }

    @Override
    public Response validate(Map<String, List<Object>> validate) {
        if (exitsEx()) {
            return this;
        }
        try {
            if (contentType == ContentType.XML) {
                assuredResponse.then().body(Validate.validateXpath(validate));
            } else if (contentType == ContentType.JSON) {
                assuredResponse.then().body(Validate.validateJson(validate));
            }
            //default
            else {
                assuredResponse.then().body(Validate.validateJson(validate));
            }
        } catch (Throwable ex) {
            this.ex = new AssertionException(ex.getMessage(), ex.getCause());
//            throw ex;
        }
        return this;
    }


    /*
     * 实际值与预期
     * @param actual
     * @param expected
     **/
    @Override
    public Response eq(Object actual, Object expected) {
        if (exitsEx()) {
            return this;
        }
        try {
            assuredResponse.then().body(Validate.validateEq(actual, expected));
        } catch (Throwable ex) {
            this.ex = new AssertionException(ex.getMessage(), ex.getCause());
//            throw ex;
        }
        return this;
    }

    @Override
    public Response eqByPath(String path, Object expected) {
        Map<String, List<Object>> validate = new HashMap<>(1);
        List<Object> data = new ArrayList<>(1);
        Map<String, Object> map = new HashMap<>(1);
        map.put(path, expected);
        data.add(map);
        validate.put(MatchesEnum.EQ.getType(), data);
        validate(validate);
        return this;
    }

    @Override
    public Response validatePlugin(String method, Object... args) {
        Map<String, List<Object>> validate = new HashMap<>(1);
        List<Object> data = new ArrayList<>(1);
        Map<String, Object> map = new HashMap<>(1);
        map.put(ValidateUtils.METHOD_NAME_KEY, method);
        map.put(ValidateUtils.METHOD_ARGS_KEY, args);
        data.add(map);
        validate.put(MatchesEnum.PLUGIN.getType(), data);
        validate(validate);
        return this;
    }

    private String getPathValue(String path) {
        if (contentType == ContentType.XML) {
            String v = assuredResponse.xmlPath().get(path);
            AssertUtils.notNull(v, "path值不存在:" + path);
            return v;
        }
        //default
        String v = assuredResponse.jsonPath().get(path).toString();
        AssertUtils.notNull(v, "path值不存在:" + path);
        return v;
    }

    private boolean exitsEx() {
        return ex != null ? Boolean.TRUE : Boolean.FALSE;
    }

}
