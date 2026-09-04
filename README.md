# OpenCode 高级用法分享材料

这套材料用于向团队介绍官方版 OpenCode 的高级用法。

## 文件入口

- [`OpenCode高级用法.md`](./OpenCode高级用法.md)：完整教程。
- [`同事分享讲稿.md`](./同事分享讲稿.md)：约 40 分钟的分享讲稿，包含 Agent 框架和多 Agent 编排视角。
- [`Oh My OpenCode Slim介绍.md`](./Oh%20My%20OpenCode%20Slim介绍.md)：第三方轻量多 Agent 编排插件介绍。
- [`现场演示步骤.md`](./现场演示步骤.md)：可以逐条复制执行的演示流程。
- [`opencode-demo/`](./opencode-demo/README.md)：可运行的 Java 21 + Spring Boot 4.1.1 示例项目。

## 建议使用方式

1. 分享前阅读完整教程。
2. 用分享讲稿组织叙事。
3. 在 `opencode-demo/` 中完成现场演示。
4. 将示例目录复制到临时位置后再进行会修改文件的演示。

> 安全提醒：不要在投屏时直接展示 `opencode debug config` 的原始输出。它可能包含从全局配置合并进来的 MCP Header、API Token 或其他凭证。

## 内容范围

示例覆盖：

- `AGENTS.md`
- OpenCode 作为 AI Agent 开发框架的定位与 Spring Boot 集成架构
- oh-my-opencode-slim 的定位、专业 Agent、安装、演示和选型边界
- Plan / Build 工作流
- 自定义 Agent
- 自定义 Command
- Skill
- 权限规则
- 模型路由
- MCP 配置
- Custom Tool
- Plugin
- 无界面运行及常驻 Server

文档按 2026-09-04 的 OpenCode 官方文档整理。工程示例采用当时的稳定版 Spring Boot 4.1.1、Java 21 和 Maven。实际使用时，请用 `opencode models --refresh` 检查当前可用模型，并根据团队环境替换示例中的 provider 和 MCP 配置。
# te
