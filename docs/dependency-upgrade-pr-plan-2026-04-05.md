# 依赖升级 PR 计划与回归策略

- 仓库: `wushunzhou/http-framework`
- 生成时间: 2026-04-05
- 目标: 为后续依赖升级 PR 提供清晰的改动边界、实施顺序、验证方法与回归策略。

## 1. 本轮 PR 的建议范围
本轮建议优先做“低风险、收益高”的升级，不把大规模平台迁移混在一次 PR 中。

### 第一批（建议先做成一个 PR）
1. `org.springframework.boot:spring-boot-starter-parent`
   - 从 `2.7.2` 升级到同系列最新稳定补丁版本。
   - 目标: 获取 Spring Boot 2.7.x 系列后续 bugfix 与安全修复。

2. `io.rest-assured:rest-assured`
   - 从 `4.3.0` 升级到更新的稳定版本。
   - 目标: 保持测试框架兼容性与断言能力。

3. `com.squareup.okhttp3:okhttp`
   - 从 `4.7.2` 升级到较新的 4.x 稳定补丁版本。
   - 目标: 改善网络层稳定性与已知问题修复。

4. `org.testng:testng`
   - 从 `6.14.3` 升级到 7.x 稳定版本。
   - 目标: 改善执行稳定性，并发与生命周期行为可控。

5. `io.qameta.allure:allure-testng`
   - 从 `2.13.2` 升级到更新补丁版本。
   - 目标: 改善测试报告生成与集成兼容性。

### 第二批（建议独立 PR，避免混入首批）
1. `com.alibaba:fastjson`
   - 建议单独拆为“迁移 PR”。
   - 原因: 涉及序列化行为与潜在代码兼容性，不适合混入常规依赖升级。

2. `javax.mail:mail`
   - 建议单独拆为“Jakarta Mail 迁移 PR”。
   - 原因: 可能涉及 `javax.*` 到 `jakarta.*` 包名调整。

3. `dom4j:dom4j`
   - 建议单独拆为“XML 兼容性 PR”。
   - 原因: 若业务对 XML 处理敏感，应隔离验证风险。

### 第三批（中长期）
- Spring Boot 3.x + JDK 17+ 升级
- Fastjson 替换为 Jackson
- 依赖扫描纳入 CI

## 2. PR 拆分原则
- 一次 PR 只解决一类风险。
- 不将“平台迁移”和“常规补丁升级”放在同一个 PR。
- 高风险库（如 fastjson）单独处理，便于回滚与专项验证。

## 3. 首批 PR 的建议改动文件
- `pom.xml`
- 可能受影响的测试基类或配置类（若 TestNG / Allure / Rest-Assured 有兼容性调整）
- 文档：`docs/dependency-scan-2026-04-05.md`
- 文档：本文件

## 4. 回归策略

### 4.1 构建验证
执行以下命令：

```bash
mvn -q -DskipTests clean compile
mvn -q test
```

如果本项目有环境区分，则补充：

```bash
mvn clean test -P qa -Dtestng.xmlFilePath=testng.xml
```

### 4.2 自动化测试回归
重点关注：
- `BaseHttpClient` 链式调用是否正常
- 动态代理调用链是否受依赖升级影响
- `RequestHttpBuilder` 中 GET / POST / PUT / DELETE / OPTIONS 请求是否全部正常
- `Response` 中断言、`saveGlobal/saveMethod/saveThread` 是否保持行为一致
- Allure 报告是否可正常生成

### 4.3 重点功能回归清单
1. **接口代理能力**
   - 通过 `@HttpServer` 声明的客户端能否正常注入与调用
   - `@Filters` 注解是否仍能正确生效

2. **请求类型回归**
   - `FormRequest`
   - `JsonRequest`
   - URL path 参数替换
   - query 参数传递
   - 默认 headers 合并逻辑

3. **断言能力回归**
   - `statusCode()`
   - `eq()`
   - `eqByPath()`
   - `validate()`
   - `validatePlugin()`

4. **上下文缓存回归**
   - ask / method / class / thread / global 五级缓存读取顺序
   - 并发执行时 ThreadLocal 行为

5. **网络与日志回归**
   - okhttp 默认请求头拦截器
   - okhttp 日志拦截器
   - websocket 场景（若有现成用例）

6. **报告回归**
   - `allure-results` 能否正常生成
   - `allure serve target/allure-results` 是否可查看

### 4.4 非功能验证
- 执行时间是否明显增加
- 测试失败率是否波动
- 构建日志中是否出现新的 deprecated 警告或反射兼容警告

## 5. 回滚策略
若升级后出现大面积失败，按如下顺序回滚：
1. 回滚 TestNG / Allure
2. 回滚 Rest-Assured / OkHttp
3. 回滚 Spring Boot 父版本

不要在问题未定位前同时继续推进 fastjson / mail / dom4j 的升级。

## 6. 推荐的后续 PR 顺序
1. PR-1: Spring Boot 2.7.x 补丁 + Rest-Assured + OkHttp + TestNG + Allure
2. PR-2: fastjson 专项迁移
3. PR-3: javax.mail / dom4j 专项升级
4. PR-4: CI 接入 dependency-check / Dependabot / Snyk
5. PR-5: 评估 Spring Boot 3.x / JDK 17 迁移

## 7. 建议的 PR 标题
- `chore: upgrade core test dependencies and add regression plan`

## 8. 建议的 PR 描述模板
```md
## 变更内容
- 升级 Spring Boot 2.7.x 补丁版本
- 升级 Rest-Assured / OkHttp / TestNG / Allure
- 补充依赖升级回归策略文档

## 风险
- 测试框架行为可能因 TestNG / Rest-Assured 版本变化受到影响
- 报告生成链路需要额外验证

## 回归范围
- 动态代理调用链
- HTTP 请求构建
- 响应断言
- 上下文缓存
- Allure 报告生成

## 验证命令
```bash
mvn -q -DskipTests clean compile
mvn -q test
mvn clean test -P qa -Dtestng.xmlFilePath=testng.xml
```
```
