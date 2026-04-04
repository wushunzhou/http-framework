# 依赖扫描报告

- 仓库: `wushunzhou/http-framework`
- 扫描时间: 2026-04-05
- 扫描范围: 顶层 `pom.xml`
- 结论摘要: 当前工程依赖整体可运行，但存在若干需要优先升级的老版本依赖，重点包括 `fastjson 1.2.70`、`javax.mail 1.4.7`、`dom4j 1.6.1` 以及较早的 `Spring Boot 2.7.2`。

## 1. 扫描依据
本次扫描依据仓库顶层 `pom.xml` 中已声明的依赖与属性版本：

- Spring Boot 父版本: `2.7.2`
- Java 版本: `1.8`
- Allure 版本: `2.13.2`
- OkHttp 版本: `4.7.2`
- Rest-Assured 版本: `4.3.0`
- TestNG 版本: `6.14.3`
- JavaMail 版本: `1.4.7`
- Hutool 版本: `5.7.22`
- MyBatis-Plus 版本: `3.5.1`
- Fastjson 版本: `1.2.70`
- Dom4j 版本: `1.6.1`

## 2. 优先级清单

### 高优先级
1. `com.alibaba:fastjson:1.2.70`
   - 风险: 老版本，历史上存在多次高危安全漏洞。
   - 建议: 优先替换为 Jackson，或迁移至 fastjson2；如果短期无法替换，至少升级到官方已修复的安全版本并补充安全基线校验。

2. `javax.mail:mail:1.4.7`
   - 风险: 版本过旧，生态已向 Jakarta Mail 演进。
   - 建议: 评估迁移到 `jakarta.mail` 体系，或先升级到兼容的较新实现版本。

3. `dom4j:dom4j:1.6.1`
   - 风险: 版本老旧，存在历史安全与维护风险。
   - 建议: 升级到 `org.dom4j:dom4j` 2.x 系列，并验证 XML 解析兼容性。

4. `org.springframework.boot:spring-boot-starter-parent:2.7.2`
   - 风险: 属于 2.7 系列较早补丁版本，建议至少升到最新 2.7.x 补丁。
   - 建议: 短期先升级到最新 2.7.x；中长期如果允许升级 JDK 到 17+，可规划迁移到 Spring Boot 3.x。

### 中优先级
5. `io.rest-assured:rest-assured:4.3.0`
   - 建议: 升级到更新的 4.x / 5.x 稳定版本，并跑完整接口回归。

6. `com.squareup.okhttp3:okhttp:4.7.2`
   - 建议: 升级到较新的 4.x 补丁版本，重点回归 websocket、日志拦截和图片/流处理场景。

7. `org.testng:testng:6.14.3`
   - 建议: 升级到 7.x 稳定版本，验证分组、并发与执行顺序。

8. `com.baomidou:mybatis-plus-boot-starter:3.5.1`
   - 建议: 升级到最新 3.5.x 补丁版本。

### 低优先级
9. `io.qameta.allure:allure-testng:2.13.2`
   - 建议: 升级到更新补丁版本，获取报告与适配修复。

10. `com.google.guava:guava:31.1-jre`
    - 建议: 可按需升级到当前稳定小版本。

11. `cn.hutool:hutool-all:5.7.22`
    - 建议: 结合实际使用模块按需升级。

12. `org.projectlombok:lombok`
    - 建议: 维持在较新的 1.18.x 稳定版本，保证与 IDE / 编译器兼容。

13. `mysql:mysql-connector-java`
    - 说明: 当前版本未在 `pom.xml` 中显式声明，实际版本受 Spring Boot 依赖管理控制。
    - 建议: 升级 Spring Boot 时同步核查其受管版本。

## 3. 建议的升级顺序
1. 先处理安全风险最高的依赖：`fastjson`、`javax.mail`、`dom4j`。
2. 然后升级基础平台与核心测试链路：`Spring Boot`、`rest-assured`、`okhttp`、`testng`。
3. 最后处理体验和维护性依赖：`allure`、`hutool`、`guava`、`lombok`。

## 4. 建议补充的自动化动作
建议在本仓库后续补充以下检查：

```bash
mvn versions:display-dependency-updates
mvn versions:display-plugin-updates
mvn org.owasp:dependency-check-maven:check
```

同时建议接入 GitHub Dependabot、Snyk 或 Sonatype 等工具，持续监控依赖更新与 CVE。

## 5. 后续动作建议
- 下一步先执行自动化版本扫描，生成“当前版本 / 可升级版本 / 是否存在安全公告”的精确表。
- 对 `fastjson` 单独出一份迁移方案（替换 Jackson 或升级到 fastjson2）。
- 评估 Spring Boot 升级路线：先 2.7.x 最新补丁，再决定是否迁移 3.x。

## 6. 备注
本报告基于仓库当前顶层 `pom.xml` 的静态扫描结果整理，不包含运行时传递依赖树、插件依赖树以及外部私服约束。如需更精确版本建议，应继续运行 Maven 版本扫描与安全扫描工具。