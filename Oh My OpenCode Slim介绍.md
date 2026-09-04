# Oh My OpenCode Slim 介绍

## 一句话定位

oh-my-opencode-slim 是 OpenCode 的第三方多 Agent 编排插件。OpenCode 提供 Agent 运行时、工具和权限等基础能力；oh-my-opencode-slim 在此基础上提供一套可直接使用的专业 Agent 团队、自动委派策略、模型预设和配套 Skill。

可以把两者理解为：

```text
OpenCode：Agent 运行时和开发平台
oh-my-opencode-slim：安装在平台上的多 Agent 编排套件
项目配置：团队自己的规则、Prompt、Skill 和权限
```

## 它主要解决什么问题

原生 OpenCode 已经允许团队自行定义 Agent，但角色划分、任务路由、并行执行、模型选择和结果汇总仍需要自己设计。oh-my-opencode-slim 提供了预设方案：由 Orchestrator 拆解任务，把搜索、研究、架构判断、界面设计和代码实现交给不同的专业 Agent，再汇总结果并继续执行。

核心价值不是“增加更多 Agent”，而是把多 Agent 的分工、路由和调度做成可复用配置。

## 主要 Agent

| Agent | 主要职责 | 模型选择思路 |
| --- | --- | --- |
| Orchestrator | 规划、委派、跟踪和汇总任务 | 使用判断力强、指令遵循稳定的模型 |
| Explorer | 搜索代码库、定位文件和模式 | 使用快速、低成本模型 |
| Oracle | 架构决策、复杂调试和技术审查 | 使用强推理模型 |
| Council | 多模型并行分析并综合结论 | 只在高价值决策中使用，注意成本 |
| Librarian | 查询外部文档、依赖和代码资料 | 使用检索效率高的模型 |
| Designer | UI/UX 设计与前端实现 | 使用视觉和前端能力较强的模型 |
| Fixer | 执行范围明确的代码修改 | 使用稳定、高效的编码模型 |

Observer 是可选的视觉分析 Agent，适合主 Orchestrator 模型不具备多模态能力时使用。

## 主要增强能力

- 后台编排：Orchestrator 可以并行派发专业 Agent，并在结果返回后继续处理。
- 模型混用：不同 Agent 可以使用不同厂商、成本和能力等级的模型。
- 模型预设：通过 `/preset` 切换整套 Agent 模型配置。
- Council：让多个模型分析同一问题，再形成综合结论。
- 内置 Skill：包括 `deepwork`、`codemap`、`verification-planning`、`worktrees`、`reflect` 等流程。
- 代码智能：集成 LSP、AST 感知搜索以及文档和代码搜索 MCP。
- 项目级定制：仓库可以覆盖 Agent、Prompt、Skill 和 MCP 分配。

## 与原生 OpenCode 的选择关系

| 选择 | 更适合的情况 | 主要代价 |
| --- | --- | --- |
| 原生 OpenCode | 希望完全掌控 Agent、Prompt、权限和流程；团队场景较简单 | 需要自己设计多 Agent 编排 |
| OpenCode + oh-my-opencode-slim | 希望快速获得成熟的专业 Agent 分工和后台委派 | 引入第三方依赖，配置、并发和 Token 消耗会增加 |
| 自建 Agent 平台 | 有特殊状态机、企业审批、长事务或强隔离要求 | 开发和维护成本最高 |

它不是 OpenCode 的替代品，也不是必须安装的组件。更合适的定位是：团队验证过原生 OpenCode 后，用它快速试验多 Agent 编排。

## 安装与验证

先预览安装会做什么：

```bash
bunx oh-my-opencode-slim@latest install --dry-run
```

交互式安装：

```bash
bunx oh-my-opencode-slim@latest install
```

没有 Bun 时也可以使用：

```bash
npx oh-my-opencode-slim@latest install
```

安装后完成模型认证并检查配置：

```bash
opencode auth login
opencode models --refresh
bunx oh-my-opencode-slim@latest doctor
opencode
```

然后在 OpenCode 中输入：

```text
ping all agents
```

安装器会修改用户级 OpenCode 配置、注册插件、生成模型预设，并可能配置 LSP 和后台子 Agent 环境。团队环境中建议先使用 `--dry-run`，检查差异后再安装。

## Spring Boot 项目的演示方式

安装完成后，可以在本示例项目中输入：

```text
分析如何为折扣接口增加固定金额优惠。请先让 Explorer 定位代码和测试，再让 Oracle 评估 BigDecimal 精度与接口兼容性，最后由 Fixer 实现并运行 Maven 测试。
```

观察重点：

1. Orchestrator 是否正确拆分任务。
2. 不同 Agent 是否拿到明确且有限的上下文。
3. 是否先分析、再实现、最后验证。
4. 并行任务是否真的带来收益，而不是重复搜索。
5. 总 Token、执行时间和结果质量是否优于单 Agent。

不建议现场自动调用 Council，因为它会并行调用多个模型，成本和等待时间更不可控。可以只展示手工命令：

```text
@council 比较“新增独立接口”和“扩展现有请求模型”两种方案，只分析，不修改代码。
```

## 团队落地建议

1. 先在非关键仓库试用，不要直接覆盖全员配置。
2. 固定插件版本，升级前阅读变更并回归验证。
3. 根据 Agent 职责分配模型，不要全部使用最昂贵模型。
4. 限制 Council、后台并行数和可用 MCP，控制成本和上下文。
5. 把团队规则放在 `AGENTS.md`，项目专属 Prompt 放在仓库配置中。
6. 对项目级配置执行代码审查，因为它能够改变 Agent 行为和工具权限。
7. 保留原生 OpenCode 配置作为回退方案。

## 可直接照读的总结话术

> OpenCode 给我们的是 Agent 运行时和扩展机制；oh-my-opencode-slim 给我们的是一套已经设计好的多 Agent 团队。它通过 Orchestrator 把搜索、研究、架构判断和实现分给不同模型，降低我们从零设计编排的成本。代价是多一层第三方依赖，也可能增加并发调用和 Token 消耗。因此它适合作为团队的可选加速层，而不是所有项目默认必须安装的基础设施。

## 官方资料

- [oh-my-opencode-slim GitHub 仓库](https://github.com/alvinunreal/oh-my-opencode-slim)
- [安装说明](https://github.com/alvinunreal/oh-my-opencode-slim/blob/master/docs/installation.md)
- [配置参考](https://github.com/alvinunreal/oh-my-opencode-slim/blob/master/docs/configuration.md)
- [项目级定制](https://github.com/alvinunreal/oh-my-opencode-slim/blob/master/docs/project-local-customization.md)
- [OpenCode v2 兼容性](https://github.com/alvinunreal/oh-my-opencode-slim/blob/master/docs/opencode-v2-compatibility.md)
