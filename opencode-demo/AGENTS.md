# OpenCode 高级用法演示项目

## 项目用途

这是一个用于向团队演示 OpenCode 高级工作流的简单 Spring Boot 项目。代码应保持精简、清晰，便于现场讲解。

## 技术栈

- Java 21
- Spring Boot 4.1.1
- Maven 3.6.3 或更高版本
- Spring MVC
- Jakarta Validation
- JUnit Jupiter、AssertJ、MockMvc

## 目录结构

- `src/main/java/com/example/opencodedemo/pricing/`：折扣计算接口、请求响应模型和业务服务。
- `src/main/java/com/example/opencodedemo/common/`：统一 API 错误结构和异常处理。
- `src/test/java/`：单元测试与 Spring MVC 集成测试。
- `.opencode/agents/`：专用 Agent。
- `.opencode/commands/`：可复用斜杠命令。
- `.opencode/skills/`：按需加载的工作流。
- `.opencode/tools/`：自定义工具。
- `.opencode/plugins/`：生命周期扩展。
- `config-snippets/`：不会被自动加载的可选配置片段。
- `scripts/`：无界面自动化示例。

## 常用命令

- 编译：`mvn compile`
- 运行全部测试：`mvn test`
- 运行服务层测试：`mvn -Dtest=PricingServiceTest test`
- 运行接口测试：`mvn -Dtest=PricingControllerTest test`
- 完整验证：`mvn verify`
- 启动应用：`mvn spring-boot:run`

## 架构约束

- Controller 只负责 HTTP 协议、参数校验和响应组装。
- 业务规则必须放在 Service 中，不能写进 Controller。
- API 请求和响应使用独立的 `record`，不要直接暴露内部实体。
- 异常统一由 `GlobalExceptionHandler` 转换为 `ApiError`。
- 当前示例没有数据库，不要为了简单需求引入持久化层。

## 编码规范

- 金额必须使用 `BigDecimal`，不能使用 `double` 或 `float`。
- 金额对外返回时保留两位小数，采用 `RoundingMode.HALF_UP`。
- 公共类和公共方法使用简洁的中文 Javadoc。
- 优先编写小型、无副作用、易测试的方法。
- 未经明确要求，不增加第三方依赖。
- 修改公共行为时，必须同步更新测试。

## 验证顺序

修改业务逻辑后：

1. 新增或更新针对性测试。
2. 先运行最相关的单个测试类。
3. 运行 `mvn test`。
4. 如涉及构建配置或多模块边界，再运行 `mvn verify`。

最终回复必须列出实际执行过的命令和结果。没有执行的检查不能声称已经通过。

## 审查标准

审查意见必须描述可以触发的真实问题，并包含具体位置、触发条件、实际影响和可行的修复建议。不要把个人风格偏好当成缺陷。

## 安全边界

- 不得读取或修改 `.env`、凭证文件和本机全局配置。
- 不得执行 `git push`、`mvn deploy`、发布或部署。
- 未经用户明确授权，不得删除文件或目录。
- 不得启用本演示项目中的占位 MCP Server。
- 未经明确要求，不得向外部系统发送项目内容。
