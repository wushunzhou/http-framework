# http-framework

## 分层思想: 分为三层,相互独立(接口,数据,用例)

一个用例可以对应很多接口， 一个接口可以对应很多数据，一个数据只能对应一个接口

## 接口层: 实现方式为动态代理,Spring容器启动后,生成代理接口注入容器

#### 编写方式:
注:如果通过通用头部等rest-assured过滤器与通用BaseUrl进行设置

eg.
```java
@HttpServer(baseUrl = "${api.url}")
@Filters({RestAssuredLogFilter.class, RequestHeadersFilter.class})
public interface RequestApiClient extends BaseHttpClient{
}
```

1. 定义一个接口类继承com.dora.httpframework.core.client.BaseHttpClient,类上注解@HttpServer注解声明该接口是一个http接口类
2. 注解
    - @HttpServer: 接口类上注解，必填，可设置整个接口类的baseUrl,可用通配符${},读取对应的application.xml内的配置
    - @Filters: 用于传递rest-assured过滤器注解，接收一个io.restassured.filter.Filter类数组， 表明整个类接口都会使用该注解里面的过滤器


## 数据层: 通过 JsonRequest.builder FormRequest.builder 的形式作为载体.
#### 编写模板:
```
FormRequest.builder()
       .name("XXX")
       .description("XXX")
       .method("post")
       .headers(Map.class)
       .path("/user/XXX")
       .reqData(data)
       .build();
```
- name:为该数据唯一标识,用例中引入需要
- type:标识该接口请求方式,目前支持(form,json)
- description:简介,标明该数据作用
- path:接口url,可以是一个带http/https的全的url,也可以是path，如果是全的Url则会覆盖掉@HttpServer(baseUrl = "${api.url}")
- method:http请求方法
- headers:接口头部,非必填 
   + 如果没有设置，则使用@Filters注入的Header，默认固定头部可继承com.dora.httpframework.core.headerfilter.HeadersFilterAdapter,重写defHeaders()方法进行设置默认固定头部
   + 如果都没有则默认[Accept=*/*, Content-Type=application/x-www-form-urlencoded; charset=UTF-8]
   + 如果都设置了，则遵循ADD原则，采取Header合并的形式，将@Filters中的Header.class和数据源中的headsers合并使用
- reqData接口请求数据，必填

## 用例层: 通过深度集成testng进行用例执行
定义一个类继承com.dora.httpframework.core.base.BaseTestCase.class,
然后通过spring @Autowired注入编写的接口层的类,再调用接口类中定义的接口,返回一个com.dora.httpframework.core.base.Response对象

###### 测试用例方法注解说明:

1. allure测试报告注解:

    - @Feature("request-api")
      类上注解, 标注主要功能模块, 可以理解为testsuite

    - @Story("登录模块接口")
      类上注解,属于feature之下的结构,可以理解为testcase

    - @Severity(SeverityLevel.CRITICAL)
      标注测试用例的重要级别
        1. Blocker级别——中断缺陷
        2. Critical级别――临界缺陷
        3. Major级别——较严重缺陷
        4. Normal级别――普通缺陷
        5. Minor级别———次要缺陷
        6. Trivial级别——轻微缺陷
        7. Enhancement级别——测试建议、其他（非缺陷）

    - @Description("请求注册用户")
      用例说明

2. testng核心驱动注解@Test:
   @Test(groups = "request-api")

3. 请求与响应
   com.dora.httpframework.core.client.BaseHttpClient对象, 一个用于发起请求http的Client
   com.dora.httpframework.core.base.Response对象, 链式调用方法处理接口返回值

   eg.

   ```
   requestApiClient.wait(TimeUnit.SECONDS, 1)
                  .saveAsk("key", "key")
                  .doHttp(FormRequest.class)
                  .getPath("XX")
                  .saveGlobal("key","key")
                  .eqByPath(XX,XX);
   ```
   - BaseHttpClient对象方法
     wait(),wait(TimeUnit unit, long interval) 用于设置请求接口前的等待时间
     saveAsk(),saveGlobal(), saveTest(),saveSuite() 用于往不同生命周期保存一个缓存,saveAsk为该请求生命周期
     doHttp(FormRequest.build)接口调用,入参为FormRequest.build,数据格式为Map类型
     doHttp(JsonRequest.build)接口调用,入参为JsonRequest.build,数据格式为Json类型
   - Response对象方法
     then(): 语法糖,无特殊意义,只用作链式调用标明请求结果开始处理
     getResponse(): 开放底层io.restassured.response.Response对象，让业务自行处理结果集
     statusCode(): 用于断言接口返回code
     - ExtractResponseOptions.class
       getHeader(String header): 获取返回头
       getJsonBody(): 获取返回值为json的body
       getXmlBody(): 获取返回值为xml的body
       getHtmlBody(): 获取返回值为html的body
       getAsTBody(Class<T> cls): 获取返回值为的T的body(类型转化)
       getHeaders():  获取所有返回头
       getStatusCode(): 获取返回状态code
       getSessionId(): 获取sessionId
       getCookie(String name): 获取cookie
       cookies(): 获取所有cookie
       getPath(String path): 获取path位置值
       getRequests(): 获取请求信息
     - SaveDataResponseOptions.class
       saveGlobal(), saveTest(),saveSuite() : 结果保存不同维度方法
     - ValidateResponseOptions.class
       validate(Map<String, List<Object>> validate): 断言方法
       validatePlugin(): 硬编码断言,用于调用方法
       eq(): 硬编码断言相等
       eqByPath(): 硬编码断言相等,值取jsonpath,xpath

## 配置文件说明: src/main/resources

application-qa.yml 区分环境配置文件,最终会根据使用环境默认合并到application.yml

- application.yml

   - notification节点: 配置通知类型(机器人通知、邮件通知等)
   - mybatis-plus节点和数据源配置: 配置数据库

- application-qa.yml
   
   - httpurl节点: 配置接口层接口类上面@HttpServer注解中baseurl代表值baseurl直接用${api.url}调用即可

## 运行方式:

- testng.xml运行
- maven运行 : mvn clean test -P uat -Dtestng.xmlFilePath=testng.xml 执行测试用例, -P 参数后面跟着需要执行的环境,默认uat, -Dtestng.xmlFilePath 可选择执行的xml文件 默认testng.xml(支持Groups方式,详情查看pom.xml)
- 调用com.ly.core.actuator.TestNgRun运行
    - 根据groups运行测试用例
    - 根据类运行测试用例
    - 根据类中方法运行测试用例
    - 可设置运行并发
- 测试报告运行: allure serve target/allure-results

## 环境搭建:

- jdk1.8 以上
- idea(需要装下maven插件与lombok插件)
- maven(一般来说idea会自带一个)
- allure2.13.2 (需要把allure目录加到ALLURE_HOME环境变量)
