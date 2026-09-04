# OpenCode 高级用法 Spring Boot 演示项目

这是一个简单的折扣计算 REST API，用于向团队演示 OpenCode 的项目规则、Agent、Command、Skill、Tool、Plugin、MCP 和自动化能力。

## 技术栈

- Java 21
- Spring Boot 4.1.1
- Maven
- Spring MVC
- Jakarta Validation
- JUnit Jupiter、AssertJ、MockMvc

Spring Boot 4 将 MVC 测试支持拆分为独立模块，因此 `pom.xml` 同时使用 `spring-boot-starter-test` 和 `spring-boot-starter-webmvc-test`。

Spring Boot 4.1.1 至少需要 Java 17 和 Maven 3.6.3。本项目统一使用 Java 21。

## 快速开始

```bash
cd opencode-demo
mvn test
opencode
```

首次使用 OpenCode 时，在 TUI 中运行：

```text
/connect
```

## 启动并调用接口

启动应用：

```bash
mvn spring-boot:run
```

调用折扣计算接口：

```bash
curl -X POST http://localhost:8080/api/prices/discount \
  -H 'Content-Type: application/json' \
  -d '{"originalPrice":100.00,"discountPercent":20}'
```

预期响应：

```json
{
  "originalPrice": 100.00,
  "discountPercent": 20,
  "finalPrice": 80.00
}
```

## 项目结构

```text
opencode-demo/
├── AGENTS.md
├── opencode.jsonc
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/example/opencodedemo/
│   │   │   ├── OpenCodeDemoApplication.java
│   │   │   ├── common/
│   │   │   └── pricing/
│   │   └── resources/application.yml
│   └── test/java/com/example/opencodedemo/pricing/
├── config-snippets/
├── scripts/
└── .opencode/
    ├── agents/
    ├── commands/
    ├── skills/
    ├── tools/
    └── plugins/
```

## 业务代码

- `PricingController`：接收并校验 HTTP 请求。
- `PricingService`：使用 `BigDecimal` 计算百分比折扣。
- `DiscountRequest`、`DiscountResponse`：API 请求和响应模型。
- `GlobalExceptionHandler`：生成统一错误响应。
- `PricingServiceTest`：不启动 Spring 的快速单元测试。
- `PricingControllerTest`：使用 MockMvc 的接口集成测试。

## 建议演示顺序

1. 询问项目结构，展示 `AGENTS.md` 自动生效。
2. 用 `@architect` 规划“固定金额优惠”功能，不修改代码。
3. 切换 Build 或调用 `@implementer` 实现功能并运行 Maven 测试。
4. 用 `/review` 触发只读审查。
5. 用 `/release-check` 触发按需 Skill。
6. 让 Agent 调用 `project` 或 `math_add` 自定义工具。
7. 请求读取 `.env`，展示 Plugin 和权限保护。
8. 运行 `scripts/headless-review.sh`，展示自动化。

如果需要介绍 oh-my-opencode-slim，可展示 [`config-snippets/oh-my-opencode-slim/`](./config-snippets/oh-my-opencode-slim/)。这些文件只是讲解示例，不会自动加载，也不会修改用户全局配置。

如果需要介绍原生 Agent 的模型分配，可展示 [`config-snippets/agent-model-routing/`](./config-snippets/agent-model-routing/)。示例使用 OpenCode 1.x 格式，并与当前自动继承模型的 Agent 配置隔离。

演示命令全部使用本项目自带 Agent，不依赖用户全局配置是否启用内置 `explore` 或 `general`。

详细步骤见上级目录的 [`现场演示步骤.md`](../现场演示步骤.md)。

## 安全说明

- MCP 示例默认禁用，因为其中的命令只是占位符。
- 自动更新和会话分享在演示配置中关闭，减少现场环境变化和误分享风险。
- `rm`、`git push`、`mvn deploy` 和常见部署命令被明确禁止。
- `.env` 读取由本地 Plugin 阻止。
- 自动化脚本只执行只读审查。

## 官方资料

- [OpenCode Config](https://opencode.ai/docs/config)
- [OpenCode Agents](https://opencode.ai/docs/agents)
- [OpenCode Commands](https://opencode.ai/docs/commands)
- [OpenCode Skills](https://opencode.ai/docs/skills)
- [OpenCode Permissions](https://opencode.ai/docs/permissions)
- [OpenCode Custom Tools](https://opencode.ai/docs/custom-tools)
- [OpenCode Plugins](https://opencode.ai/docs/plugins)
- [Spring Boot 文档](https://docs.spring.io/spring-boot/)
- [Spring Boot 系统要求](https://docs.spring.io/spring-boot/system-requirements.html)
