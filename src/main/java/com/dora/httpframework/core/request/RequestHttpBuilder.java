package com.dora.httpframework.core.request;

import com.dora.httpframework.exception.DoraException;
import com.dora.httpframework.utils.SomeUtils;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.SSLConfig;
import io.restassured.filter.Filter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileInputStream;
import java.security.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;

/**
 * @Author: Dora
 * @Description: rest assured http 核心-处理构建类
 * @Date: 2021/12/13
 */
public class RequestHttpBuilder {
    private volatile static RequestHttpBuilder builder = null;

    public static RequestHttpBuilder create() {
        if (builder == null) {
            //  同步锁
            synchronized (RequestHttpBuilder.class) {
                if (builder == null) {
                    builder = new RequestHttpBuilder();
                }
            }
        }
        return builder;
    }

    private RequestHttpBuilder() {
    }

    /**
     * @Description 默认URL
     * @Data 2021/12/22
     * @Param [baseUrl]
     **/
    public RequestHttpBuilder setBaseUrl(String baseUrl) {
        RestAssured.baseURI = baseUrl;
        return this;
    }

    public String getBaseUrl() {
        return RestAssured.baseURI;
    }

    /**
     * @Description 默认端口号
     * @Data 2021/12/23
     * @Param [baseHost, basePort]
     **/
    public RequestHttpBuilder setProt(int basePort) {
        RestAssured.port = basePort;
        return this;
    }

    /**
     * @Description 默认过滤器
     * @Data 2021/12/23
     * @Param [filters]
     **/
    public RequestHttpBuilder setFilter(Filter... filters) {
        if (filters != null && filters.length > 0) {
            RestAssured.replaceFiltersWith(Arrays.asList(filters));
        }
        return this;
    }


    public Response get(String url, Filter... filters) {
        return get(null, url, filters);
    }

    public Response get(Map<String, ?> parameters, String url, Filter... filters) {
        return get(null, parameters, url, filters);
    }

    public Response get(Map<String, ?> headers, Map<String, ?> parameters, String url, Filter... filters) {
        return get(headers, parameters, url, 5000, 5000, 5000, true, filters);
    }

    public Response get(String url, int connectTimeout, int requestTimeout, int socketTimeout, boolean redirectsEnabled, Filter... filters) {
        return get(null, null, url, connectTimeout, requestTimeout, socketTimeout, redirectsEnabled, filters);
    }

    public Response get(Map<String, ?> parameters, String url, int connectTimeout, int requestTimeout, int socketTimeout, boolean redirectsEnabled, Filter... filters) {
        return get(null, parameters, url, connectTimeout, requestTimeout, socketTimeout, redirectsEnabled, filters);
    }

    /**
     * get请求
     *
     * @param headers
     * @param parameters
     * @param url
     * @param connectTimeout   设置连接超时时间
     * @param requestTimeout   设置请求超时时间
     * @param socketTimeout
     * @param redirectsEnabled 默认允许自动重定向
     * @param filters          过滤器
     * @return
     */
    public Response get(Map<String, ?> headers, Map<String, ?> parameters, String url,
                        int connectTimeout, int requestTimeout, int socketTimeout, boolean redirectsEnabled, Filter... filters) {
        config().httpClient(createConfig(connectTimeout, requestTimeout, socketTimeout, redirectsEnabled));

        RequestSpecification restHandle = given();

        //default content-type 如果yaml中headers中存在content-type 将覆盖该配置
        restHandle.config(config().encoderConfig(EncoderConfig.encoderConfig().defaultCharsetForContentType("UTF-8", ContentType.URLENC)));

        if (headers != null) {
            restHandle.headers(headers);
        }

        if (filters != null && filters.length > 0) {
            restHandle.filters(Arrays.asList(filters));
        }

        if (parameters != null) {
            if (SomeUtils.isExistBrace(url)) {
                // /order/{orderId}/{driverId}
                return restHandle.when().get(url, parameters);
            } else {
                // /order     {"orderId", "11111"} {"driverId", "22222"}
                restHandle.params(parameters);
                return restHandle.when().get(url);
            }
        } else {
            return restHandle.when().get(url);
        }
    }

    public Response put(Map<String, ?> parameters, String url, Filter... filters) {
        return put(null, parameters, url, filters);
    }

    public Response put(Map<String, ?> headers, Map<String, ?> parameters, String url, Filter... filters) {
        return put(headers, parameters, null, url, filters);
    }

    public Response put(Map<String, ?> headers, Map<String, ?> parameters, String contentType, String url, Filter... filters) {
        return put(headers, parameters, contentType, url, 5000, 5000, 5000, true, filters);
    }

    /**
     * put请求  默认json请求
     *
     * @param headers
     * @param data
     * @param url
     * @param filters
     * @return
     */
    public Response put(Map<String, ?> headers, String data, String url, Filter... filters) {
        return put(headers, data, ContentType.JSON, url, 5000, 5000, 5000, true, filters);
    }

    /**
     * put请求 json或者xml模式
     *
     * @param headers
     * @param data
     * @param contentType 只支持ContentType.JSON, ContentType.XML
     * @param url
     * @param filters
     * @return
     */
    public Response put(Map<String, ?> headers, String data, ContentType contentType, String url, Filter... filters) {
        return put(headers, data, contentType, url, 5000, 5000, 5000, true, filters);
    }

    /**
     * put请求  json, xml, text模式
     *
     * @param headers
     * @param data
     * @param contentType      只支持ContentType.JSON, ContentType.XML, ContentType.TEXT
     * @param url
     * @param connectTimeout
     * @param requestTimeout
     * @param socketTimeout
     * @param redirectsEnabled
     * @param filters
     * @return
     */
    public Response put(Map<String, ?> headers, String data, ContentType contentType, String url,
                        int connectTimeout, int requestTimeout, int socketTimeout, boolean redirectsEnabled, Filter... filters) {
        config().httpClient(createConfig(connectTimeout, requestTimeout, socketTimeout, redirectsEnabled));

        if (contentType != ContentType.JSON && contentType != ContentType.XML && contentType != ContentType.TEXT) {
            throw new DoraException("content-type错误,只支持JSON,XML,TEXT");
        }

        RequestSpecification restHandle = given().contentType(contentType);

        if (headers != null) {
            restHandle.headers(headers);
        }

        restHandle.body(data);

        if (filters != null && filters.length > 0) {
            restHandle.filters(Arrays.asList(filters));
        }
        return restHandle.when().put(url);
    }

    /**
     * @param headers
     * @param parameters
     * @param url
     * @param connectTimeout
     * @param requestTimeout
     * @param socketTimeout
     * @param redirectsEnabled
     * @param filters
     * @return
     */
    public Response put(Map<String, ?> headers, Map<String, ?> parameters, String contentType, String url,
                        int connectTimeout, int requestTimeout, int socketTimeout, boolean redirectsEnabled, Filter... filters) {
        config().httpClient(createConfig(connectTimeout, requestTimeout, socketTimeout, redirectsEnabled));

        RequestSpecification restHandle = given();
        if (!StringUtils.isBlank(contentType)) {
            restHandle.config(config().encoderConfig(encoderConfig().defaultCharsetForContentType("UTF-8", ContentType.URLENC)))
                    .contentType(contentType);
            restHandle.contentType(contentType);
        } else {
            restHandle.config(config().encoderConfig(EncoderConfig.encoderConfig().defaultCharsetForContentType("UTF-8", ContentType.URLENC)));
        }

        if (headers != null) {
            restHandle.headers(headers);
        }

        if (filters != null && filters.length > 0) {
            restHandle.filters(Arrays.asList(filters));
        }

        if (parameters != null) {
            if (SomeUtils.isExistBrace(url)) {
                // /order/{orderId}/{driverId}
                return restHandle.when().put(url, parameters);
            } else {
                // /order     {"orderId", "11111"} {"driverId", "22222"}
                restHandle.params(parameters);
                return restHandle.when().put(url);
            }
        } else {
            return restHandle.when().put(url);
        }
    }

    public Response delete(Map<String, ?> parameters, String url, Filter... filters) {
        return delete(null, parameters, url, filters);
    }

    public Response delete(Map<String, ?> headers, Map<String, ?> parameters, String url, Filter... filters) {
        return delete(headers, parameters, url, 5000, 5000, 5000, true, filters);
    }

    /**
     * @param headers
     * @param parameters
     * @param url
     * @param connectTimeout
     * @param requestTimeout
     * @param socketTimeout
     * @param redirectsEnabled
     * @param filters
     * @return
     */
    public Response delete(Map<String, ?> headers, Map<String, ?> parameters, String url,
                           int connectTimeout, int requestTimeout, int socketTimeout, boolean redirectsEnabled, Filter... filters) {
        config().httpClient(createConfig(connectTimeout, requestTimeout, socketTimeout, redirectsEnabled));

        RequestSpecification restHandle = given()
                .config(config().encoderConfig(encoderConfig().defaultCharsetForContentType("UTF-8", ContentType.URLENC)));

        if (headers != null) {
            restHandle.headers(headers);
        }

        if (filters != null && filters.length > 0) {
            restHandle.filters(Arrays.asList(filters));
        }

        if (parameters != null) {
            if (SomeUtils.isExistBrace(url)) {
                // /order/{orderId}/{driverId}
                return restHandle.when().delete(url, parameters);
            } else {
                // /order     {"orderId", "11111"} {"driverId", "22222"}
                restHandle.params(parameters);
                return restHandle.when().delete(url);
            }
        } else {
            return restHandle.when().delete(url);
        }
    }

    public Response post(Map<String, ?> headers, Map<String, ?> parameters, String url, Filter... filters) {
        return post(headers, parameters, null, url, filters);
    }

    public Response post(Map<String, ?> parameters, String url, Filter... filters) {
        return post(null, parameters, null, url, filters);
    }

    public Response post(Map<String, ?> headers, Map<String, ?> parameters, String contentType, String url, Filter... filters) {
        return post(headers, parameters, contentType, url, 5000, 5000, 5000, true, filters);
    }

    /**
     * post请求表单模式
     *
     * @param headers
     * @param parameters
     * @param contentType
     * @param url
     * @param connectTimeout   设置连接超时时间
     * @param requestTimeout   设置请求超时时间
     * @param socketTimeout
     * @param redirectsEnabled 重定向
     * @param filters          过滤器
     * @return
     */
    public Response post(Map<String, ?> headers, Map<String, ?> parameters, String contentType, String url,
                         int connectTimeout, int requestTimeout, int socketTimeout, boolean redirectsEnabled, Filter... filters) {
        config().httpClient(createConfig(connectTimeout, requestTimeout, socketTimeout, redirectsEnabled));

        RequestSpecification restHandle = given();
        if (!StringUtils.isBlank(contentType)) {
            restHandle.config(config().encoderConfig(encoderConfig().defaultCharsetForContentType("UTF-8", ContentType.URLENC)))
                    .contentType(contentType);
        } else {
            restHandle.config(config().encoderConfig(EncoderConfig.encoderConfig().defaultCharsetForContentType("UTF-8", ContentType.URLENC)));
        }

        if (headers != null) {
            restHandle.headers(headers);
        }

        if (parameters != null) {
            restHandle.formParams(parameters);
        }

        if (filters != null && filters.length > 0) {
            restHandle.filters(Arrays.asList(filters));
        }
        return restHandle.when().post(url);
    }

    /**
     * post请求 默认json请求
     *
     * @param data
     * @param url
     * @param filters
     * @return
     */
    public Response post(String data, String url, Filter... filters) {
        return post(null, data, url, filters);
    }

    /**
     * post请求 json,xml,text模式
     *
     * @param headers
     * @param data
     * @param contentType 只支持ContentType.JSON, ContentType.XML, ContentType.TEXT
     * @param url
     * @param filters
     * @return
     */
    public Response post(Map<String, ?> headers, String data, ContentType contentType, String url, Filter... filters) {
        return post(headers, data, contentType, url, 5000, 5000, 5000, true, filters);
    }

    /**
     * post请求 默认json请求
     *
     * @param headers
     * @param data
     * @param url
     * @param filters
     * @return
     */
    public Response post(Map<String, ?> headers, String data, String url, Filter... filters) {
        return post(headers, data, ContentType.JSON, url, 5000, 5000, 5000, true, filters);
    }

    /**
     * post请求，入参为json,xml,text
     *
     * @param headers
     * @param data
     * @param url
     * @return
     */
    public Response post(Map<String, ?> headers, String data, ContentType contentType, String url, int connectTimeout, int requestTimeout, int socketTimeout,
                         boolean redirectsEnabled, Filter... filters) {
        config().httpClient(createConfig(connectTimeout, requestTimeout, socketTimeout, redirectsEnabled));

        if (contentType != ContentType.JSON && contentType != ContentType.XML && contentType != ContentType.TEXT) {
            throw new DoraException("content-type错误,只支持JSON,XML,TEXT");
        }

        RequestSpecification restHandle = given().contentType(contentType);

        if (headers != null) {
            restHandle.headers(headers);
        }

        if (data != null) {
            restHandle.body(data);
        }

        if (filters != null && filters.length > 0) {
            restHandle.filters(Arrays.asList(filters));
        }

        return restHandle.when().post(url);
    }

    public Response options(Map<String, ?> headers, String url, Filter... filters) {
        return options(headers, url, 5000, 5000, 5000, true, filters);
    }

    /**
     * options请求，无参
     *
     * @param headers
     * @param url
     * @return
     */
    public Response options(Map<String, ?> headers, String url, int connectTimeout, int requestTimeout, int socketTimeout,
                            boolean redirectsEnabled, Filter... filters) {

        config().httpClient(createConfig(connectTimeout, requestTimeout, socketTimeout, redirectsEnabled));


        RequestSpecification restHandle = given();

        if (headers != null) {
            restHandle.headers(headers);
        }

        if (filters != null && filters.length > 0) {
            restHandle.filters(Arrays.asList(filters));
        }

        return restHandle.when().options(url);
    }


    /**
     * post请求 默认json请求
     *
     * @param data
     * @param url
     * @param filters
     * @return
     */
    public Response post(byte[] data, String url, Filter... filters) {
        return post(null, data, url, filters);
    }

    /**
     * post请求 json,xml,text模式
     *
     * @param headers
     * @param data
     * @param contentType 只支持ContentType.JSON, ContentType.XML, ContentType.TEXT
     * @param url
     * @param filters
     * @return
     */
    public Response post(Map<String, ?> headers, byte[] data, ContentType contentType, String url, Filter... filters) {
        return post(headers, data, contentType, url, 5000, 5000, 5000, true, filters);
    }

    /**
     * post请求 默认json请求
     *
     * @param headers
     * @param data
     * @param url
     * @param filters
     * @return
     */
    public Response post(Map<String, ?> headers, byte[] data, String url, Filter... filters) {
        return post(headers, data, ContentType.JSON, url, 5000, 5000, 5000, true, filters);
    }


    /**
     * post请求，入参为json,xml,text
     *
     * @param headers
     * @param data    [byte]
     * @param url
     * @return
     */
    public Response post(Map<String, ?> headers, byte[] data, ContentType contentType, String url, int connectTimeout, int requestTimeout, int socketTimeout,
                         boolean redirectsEnabled, Filter... filters) {
        config().httpClient(createConfig(connectTimeout, requestTimeout, socketTimeout, redirectsEnabled));

        if (contentType != ContentType.JSON && contentType != ContentType.XML && contentType != ContentType.TEXT) {
            throw new DoraException("content-type错误,只支持JSON,XML,TEXT");
        }

        RequestSpecification restHandle = given().contentType(contentType);

        if (headers != null) {
            restHandle.headers(headers);
        }

        if (data != null) {
            restHandle.body(data);
        }

        if (filters != null && filters.length > 0) {
            restHandle.filters(Arrays.asList(filters));
        }

        return restHandle.when().post(url);
    }


    private HttpClientConfig createConfig(int connectTimeout, int requestTimeout, int socketTimeout, boolean redirectsEnabled) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(requestTimeout)
                .setSocketTimeout(socketTimeout)
                .setRedirectsEnabled(redirectsEnabled)
                .build();

        return HttpClientConfig.httpClientConfig().httpClientFactory(() ->
                HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build());
    }

    /**
     * sslConfig设置
     */
    private void sslConfig(String certPath, String password) {
        KeyStore keyStore = null;
        SSLConfig config = null;

        try {
            keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(
                    new FileInputStream(certPath),
                    password.toCharArray());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!Objects.isNull(keyStore)) {
            org.apache.http.conn.ssl.SSLSocketFactory clientAuthFactory = null;
            try {
                clientAuthFactory = new org.apache.http.conn.ssl.SSLSocketFactory(keyStore, password);
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException |
                     UnrecoverableKeyException e) {
                e.printStackTrace();
            }
            config = new SSLConfig().with().sslSocketFactory(clientAuthFactory).and().allowAllHostnames();
        }
        RestAssured.config = RestAssured.config().sslConfig(config);
    }

    /**
     * ssl忽略设置
     *
     * @param restHandle
     */
    private void relaxedSSL(RequestSpecification restHandle) {
        RestAssured.useRelaxedHTTPSValidation();
    }
}
