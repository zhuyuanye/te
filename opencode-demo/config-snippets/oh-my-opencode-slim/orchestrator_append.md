# Spring Boot 项目补充规则

处理本仓库任务时：

1. 开始前先阅读根目录 `AGENTS.md`。
2. 先让 Explorer 定位相关 Java 类和测试，不要重复扫描整个仓库。
3. 涉及金额、接口兼容性或架构调整时，让 Oracle 评估风险。
4. 只有需求和验收条件明确后，才将范围有限的实现任务交给 Fixer。
5. Java 代码必须遵守 Controller、Service 和请求响应模型的分层边界。
6. 金额必须使用 `BigDecimal`，不得使用 `double` 或 `float`。
7. 完成后先运行针对性测试，再运行 `mvn test`。
8. 不得执行 `git push`、`mvn deploy` 或任何部署操作。

最终回复必须说明各 Agent 的分工、修改文件、测试结果和剩余风险。
