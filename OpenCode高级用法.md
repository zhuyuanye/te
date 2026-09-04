# OpenCode 高级用法

> 本文以官方版 OpenCode（[opencode.ai](https://opencode.ai)）为主，并单独介绍第三方插件 oh-my-opencode-slim；不讨论完整 oh-my-opencode 套件。
>
> 配套材料：[`opencode-demo/`](./opencode-demo/README.md)、[`同事分享讲稿.md`](./同事分享讲稿.md)、[`现场演示步骤.md`](./现场演示步骤.md)。
>
> 本文所有工程示例均基于 Java 21、Spring Boot 4.1.1 和 Maven。

OpenCode 高级用法的核心不是把提示词写得更长，而是把项目规范、角色、权限和重复流程固化下来。

## 1. OpenCode 的扩展层次

| 能力 | 适合放什么 |
| --- | --- |
| `AGENTS.md` | 项目架构、代码规范、测试命令、禁止事项 |
| Agent | 不同角色、模型、权限，例如规划、开发、审查 |
| Command | `/review`、`/test` 这种可重复工作流 |
| Skill | 按需加载的复杂操作手册 |
| MCP | GitHub、数据库、浏览器等外部系统 |
| Custom Tool | 项目专用的可调用函数 |
| Plugin | 监听事件、修改 OpenCode 行为 |

推荐的项目结构：

```text
project/
├── AGENTS.md
├── opencode.jsonc
├── pom.xml
├── src/
│   ├── main/java/
│   └── test/java/
└── .opencode/
    ├── agents/
    │   ├── reviewer.md
    │   └── debugger.md
    ├── commands/
    │   ├── review.md
    │   └── test-fix.md
    ├── skills/
    │   └── git-release/SKILL.md
    ├── tools/
    │   └── database.ts
    └── plugins/
        └── env-protection.js
```

## 2. 把 OpenCode 作为 AI Agent 开发框架

除了直接使用 TUI，OpenCode 也可以作为面向研发场景的 Agent 运行时。它已经提供了构建 Agent 产品所需的主要组件：

- Agent Loop：模型可以读取环境、调用工具、观察结果并继续执行。
- 角色与知识：Agent、`AGENTS.md`、Command 和 Skill。
- 行动能力：内置工具、Custom Tool 和 MCP。
- 状态管理：Session、Message、子会话、取消和代码差异。
- 安全治理：按 Agent、工具、命令和路径控制权限。
- 程序化接口：无界面 Server、OpenAPI、事件流和 JavaScript/TypeScript SDK。

在 Java 团队中，推荐把 Spring Boot 作为控制面，把 OpenCode 作为执行面：

```text
前端 / 企业内部平台
        ↓
Spring Boot
鉴权、任务、队列、Prompt 模板、审计、持久化
        ↓ HTTP / SSE
OpenCode Server
Session、Agent、Skill、工具调用、权限
        ↓
Git worktree / Maven / Custom Tool / MCP
```

最小实现流程：

1. 使用 `opencode serve` 启动无界面服务，并配置 Basic Auth。
2. Spring Boot 使用 `WebClient` 调用 `POST /session` 创建会话。
3. 调用 `/session/{id}/message` 同步执行，或调用 `/session/{id}/prompt_async` 异步执行。
4. 通过 `/event` 的 SSE 事件更新任务进度，通过 Session API 获取消息、状态和代码差异。
5. 每个任务使用独立目录或 Git worktree，并通过权限禁止未授权的推送、部署和敏感文件访问。

OpenCode 的 OpenAPI 规范可以从 Server 的 `/doc` 查看，Java 项目可以据此生成 Client，也可以直接使用 Spring `WebClient`。官方现有稳定 SDK 主要面向 JavaScript/TypeScript；V2 嵌入式 SDK 仍是 Beta，应谨慎用于关键生产系统。

这种方案适合代码审查、测试生成、自动修复、仓库问答和研发自动化。对于通用客服、严格 DAG 编排或跨天运行的业务流程，更合理的方式通常是由专门的业务工作流框架负责总编排，把 OpenCode 作为其中处理代码任务的执行节点。

参考：[Server 文档](https://opencode.ai/docs/server/)、[SDK 文档](https://opencode.ai/docs/sdk/)

## 3. oh-my-opencode-slim：框架之上的多 Agent 编排层

[oh-my-opencode-slim](https://github.com/alvinunreal/oh-my-opencode-slim) 是 OpenCode 的第三方 Agent 编排插件。它不会替代 OpenCode 的运行时，而是在原生 Agent、Tool、Skill、MCP 和权限体系之上提供一套预设的专业 Agent 团队。

它的核心工作方式是：Orchestrator 规划任务并把子任务交给 Explorer、Oracle、Librarian、Designer、Fixer 等专业 Agent；Council 可以让多个模型并行分析同一问题。不同 Agent 可以使用不同模型，从而按任务平衡质量、速度和成本。

适合引入它的情况：

- 已经掌握原生 OpenCode，希望快速验证多 Agent 编排。
- 任务经常包含搜索、研究、决策、实现和验证等不同阶段。
- 希望按 Agent 混用不同模型或供应商。
- 需要后台委派、模型预设、worktree 和配套 Skill。

需要评估的代价：

- 它是第三方依赖，需要单独关注版本、兼容性和安全更新。
- 自动委派和并行 Agent 可能增加 Token、延迟和排障复杂度。
- 项目级配置和 Prompt 能改变 Agent 行为，应像代码一样审查。
- 简单任务可能由单 Agent 更快完成，不必强制编排。

安装前建议先运行只读预览：

```bash
bunx oh-my-opencode-slim@latest install --dry-run
```

本地材料提供了完整中文介绍和不会自动加载的配置示例：

- [`Oh My OpenCode Slim介绍.md`](./Oh%20My%20OpenCode%20Slim介绍.md)
- [`config-snippets/oh-my-opencode-slim/`](./opencode-demo/config-snippets/oh-my-opencode-slim/)

参考：[项目仓库](https://github.com/alvinunreal/oh-my-opencode-slim)、[安装说明](https://github.com/alvinunreal/oh-my-opencode-slim/blob/master/docs/installation.md)、[配置参考](https://github.com/alvinunreal/oh-my-opencode-slim/blob/master/docs/configuration.md)

## 4. Plan / Build 工作流

OpenCode 内置以下常用 Agent：

- `Plan`：分析和规划，默认限制文件修改及命令执行。
- `Build`：真正实现功能。
- `Explore`：只读搜索代码。
- `Scout`：研究外部文档和依赖源码。
- `General`：处理多步骤子任务。

在 TUI 中按 `Tab` 切换主 Agent，也可以直接调用子 Agent：

```text
@architect 找出折扣请求从 Controller 到 Service 的完整调用链，只分析不修改
```

复杂改动推荐先进入 Plan 模式：

```text
先进入 Plan 模式。

目标：
为折扣接口增加固定金额优惠。

请先：
1. 找到现有 Controller、Service、请求响应模型和测试
2. 列出需要修改的类和方法
3. 标注 BigDecimal 精度和接口兼容性风险
4. 给出单元测试与 MockMvc 测试矩阵

限制：
- 不改变现有百分比优惠接口
- 不增加运行时依赖
- 暂时不要写代码
```

计划确认后切换 Build：

```text
按照计划实现。每完成一个阶段运行对应测试；
不要修改计划之外的文件，最后给出变更摘要和验证结果。
```

参考：[Agents 文档](https://opencode.ai/docs/agents)

## 5. 用 AGENTS.md 固化项目知识

在 OpenCode TUI 中运行：

```text
/init
```

它会分析仓库并生成或改进 `AGENTS.md`。建议把这个文件提交到 Git。OpenCode 还支持全局的 `~/.config/opencode/AGENTS.md`，从而分开管理项目规则和个人规则。

一个实用模板：

```markdown
# 项目规则

## 技术栈

- Java 21
- Spring Boot 4.1.1
- Maven
- Spring MVC、Jakarta Validation

## 架构约束

- Controller 只处理 HTTP、参数校验和响应组装
- 业务规则必须放在 Service
- 请求响应使用独立 record
- 金额必须使用 BigDecimal

## 常用命令

- 编译：`mvn compile`
- 全部测试：`mvn test`
- 指定测试：`mvn -Dtest=PricingServiceTest test`
- 完整验证：`mvn verify`

## 验证顺序

修改完成后依次运行：

1. 相关单元测试
2. 相关 MockMvc 测试
3. `mvn test`
4. 构建配置发生变化时运行 `mvn verify`

## 安全边界

- 不读取或修改 `.env`
- 不执行数据库迁移
- 不执行 `git push`、`mvn deploy` 或部署
```

比起只写“请生成高质量代码”，明确告诉 OpenCode 命令、边界和验收条件更有效。

参考：[Rules 文档](https://opencode.ai/docs/rules)

## 6. 定制专用 Agent

创建 `.opencode/agents/reviewer.md`：

```markdown
---
description: 只读审查当前改动，关注正确性、安全性和回归风险
mode: subagent
model: openai/gpt-5.6-sol
reasoningEffort: high
temperature: 0.1
permission:
  edit: deny
  bash:
    "*": ask
    "git diff*": allow
    "git status*": allow
---

你是本项目的代码审查员。

先阅读 AGENTS.md，然后检查当前 diff。按严重程度输出问题。
每个问题必须包含：

- 文件和位置
- 触发条件
- 实际后果
- 建议修复方式

不要因为个人风格偏好提出问题。
如果没有实质性问题，明确说明。
```

其中 `model` 使用 `provider/model-id` 格式。上面的模型只是当前示例，复制前必须运行 `opencode models` 确认是否可用。OpenCode 1.x 可以把供应商特定的 `reasoningEffort` 放在 Agent 配置中；V2 使用 `provider/model-id#variant` 形式，例如 `openai/gpt-5.6-sol#high`。

完整的可复制示例见 [`config-snippets/agent-model-routing/`](./opencode-demo/config-snippets/agent-model-routing/)。

使用：

```text
@reviewer 审查当前未提交改动
```

也可以通过交互方式创建 Agent：

```bash
opencode agent create
```

实用的 Agent 组合：

- `architect`：只读、强模型，负责方案设计。
- `implementer`：允许编辑，但限制危险 shell。
- `reviewer`：只读审查 diff。
- `debugger`：允许运行测试和日志命令。
- `docs`：只允许修改文档目录。

## 7. 用自定义命令封装重复流程

创建 `.opencode/commands/review.md`：

````markdown
---
description: 审查指定范围的改动
agent: reviewer
---

审查以下范围：

$ARGUMENTS

当前状态：

!`git status --short`

当前差异：

!`git diff --stat`

找出真实的正确性、安全性、兼容性或性能问题。
不要直接修改代码。
````

运行：

```text
/review PricingService、PricingController 及其测试
```

命令支持：

- `$ARGUMENTS`：全部参数。
- `$1`、`$2`：位置参数。
- `` !`command` ``：把 shell 输出注入提示词。
- `@file`：引用文件。

修复失败测试的命令示例：

````markdown
---
description: 运行 Maven 测试并修复失败
agent: debugger
---

测试输出：

!`mvn test`

根据证据分析根因，实施最小修复，然后重新运行目标测试类和完整测试。
````

自定义命令可以放在项目 `.opencode/commands/`，也可以放在全局 `~/.config/opencode/commands/`。

参考：[Commands 文档](https://opencode.ai/docs/commands)

## 8. 精细控制权限

不要简单地把所有操作都设成允许。更稳妥的策略是：

```jsonc
{
  "$schema": "https://opencode.ai/config.json",

  "permission": {
    "*": "ask",

    "read": "allow",
    "glob": "allow",
    "grep": "allow",
    "lsp": "allow",

    "bash": {
      "*": "ask",
      "git status*": "allow",
      "git diff*": "allow",
      "git log*": "allow",
      "mvn test*": "allow",
      "mvn *test*": "allow",
      "mvn verify*": "allow",
      "rm *": "deny",
      "git push*": "deny",
      "mvn deploy*": "deny"
    },

    "edit": {
      "*": "ask",
      ".env*": "deny"
    }
  }
}
```

三种结果分别是：

- `allow`：自动执行。
- `ask`：需要确认。
- `deny`：禁止执行。

`opencode --auto` 只会自动批准原本为 `ask` 的操作，显式 `deny` 仍然有效。

参考：[Permissions 文档](https://opencode.ai/docs/permissions)

## 9. 按任务分配模型

不要让昂贵模型做所有工作：

```jsonc
{
  "$schema": "https://opencode.ai/config.json",
  "model": "provider/main-model",
  "small_model": "provider/fast-model"
}
```

进一步把模型配置到 Agent 或 Command：

- Explore、简单查找：快速便宜模型。
- 架构设计、复杂调试：强推理模型。
- 实现：工具调用可靠的编码模型。
- 代码审查：低温度、强推理模型。

查询真实可用的模型名称：

```bash
opencode models --refresh
opencode models --verbose
```

临时指定模型：

```bash
opencode -m provider/model
opencode run -m provider/model "审查当前模块"
```

OpenCode 还支持给模型定义 reasoning effort 等 variant。不同模型支持的 variant 不同，不应直接假设名称。

参考：[Models 文档](https://opencode.ai/docs/models)

## 10. MCP 要少而精

配置本地 MCP：

```jsonc
{
  "$schema": "https://opencode.ai/config.json",

  "mcp": {
    "project-db": {
      "type": "local",
      "command": ["npx", "-y", "my-mcp-server"],
      "enabled": true,
      "environment": {
        "DATABASE_URL": "{env:DATABASE_URL}"
      }
    }
  }
}
```

管理命令：

```bash
opencode mcp add
opencode mcp list
opencode mcp auth server-name
opencode mcp debug server-name
```

每个 MCP 的工具说明都会占用上下文。GitHub 一类 MCP 往往暴露大量工具，容易浪费 token；建议按项目启用，把暂时没用的服务器设置为 `enabled: false`。

参考：[MCP 文档](https://opencode.ai/docs/mcp-servers)

## 11. Skills：把复杂流程变成按需知识

创建 `.opencode/skills/git-release/SKILL.md`：

```markdown
---
name: git-release
description: 准备项目版本发布，包括检查、版本号和发布说明
---

# Git 发布检查流程

1. 确认工作区干净
2. 读取最近一个 tag
3. 汇总此后的用户可见变更
4. 运行完整测试
5. 更新版本号和 CHANGELOG
6. 输出发布检查表

未经明确要求，不执行推送、打标签、发布制品或部署。
```

Skill 和 `AGENTS.md` 的区别：

- `AGENTS.md` 始终进入项目上下文。
- Skill 只在匹配任务时加载，适合发布、迁移、事故处理等较长流程。

这能减少常驻上下文，提高执行稳定性。

参考：[Agent Skills 文档](https://opencode.ai/docs/skills)

## 12. 无界面运行和自动化

单次非交互执行：

```bash
opencode run "检查当前改动中是否存在明显回归"
```

输出 JSON 事件，便于脚本处理：

```bash
opencode run --format json \
  --agent reviewer \
  "审查当前 Git diff"
```

延续或分叉会话：

```bash
opencode -c
opencode --session SESSION_ID
opencode --session SESSION_ID --fork
```

频繁运行时，可以保持服务常驻，避免 MCP 每次冷启动：

```bash
opencode serve
```

另一个终端：

```bash
opencode run \
  --attach http://localhost:4096 \
  "运行快速代码审查"
```

CLI 还支持 session、export/import、GitHub automation、Web 服务等能力。

参考：[CLI 文档](https://opencode.ai/docs/cli)

## 13. Custom Tool 与 Plugin

当 MCP 太重、普通命令又不够时，可以创建项目专用工具。工具放在 `.opencode/tools/`，文件名会成为工具名：

```ts
import { tool } from "@opencode-ai/plugin"

export default tool({
  description: "查询项目数据库",
  args: {
    query: tool.schema.string().describe("只读 SQL 查询"),
  },
  async execute(args) {
    return `已校验只读查询：${args.query}`
  },
})
```

参考：[Custom Tools 文档](https://opencode.ai/docs/custom-tools)

插件适合监听事件、记录用量、接入监控或改变默认行为：

- 项目插件：`.opencode/plugins/`
- 全局插件：`~/.config/opencode/plugins/`
- npm 插件：在 `opencode.json` 的 `plugin` 数组中声明

参考：[Plugins 文档](https://opencode.ai/docs/plugins)

## 14. 推荐的落地顺序

如果只做四件事，建议依次完成：

1. 用 `/init` 生成并完善 `AGENTS.md`。
2. 创建只读 `reviewer` Agent。
3. 添加 `/review`、`/test-fix` 两个命令。
4. 用权限规则禁止 `.env`、`rm`、`git push`、`mvn deploy` 和部署操作。

这套组合通常已经能让 OpenCode 从“聊天式写代码”升级成稳定的项目工作流。

## 官方资料

- [OpenCode 文档首页](https://opencode.ai/docs)
- [Config](https://opencode.ai/docs/config)
- [Agents](https://opencode.ai/docs/agents)
- [Rules](https://opencode.ai/docs/rules)
- [Commands](https://opencode.ai/docs/commands)
- [Permissions](https://opencode.ai/docs/permissions)
- [MCP Servers](https://opencode.ai/docs/mcp-servers)
- [Agent Skills](https://opencode.ai/docs/skills)
- [CLI](https://opencode.ai/docs/cli)
- [Custom Tools](https://opencode.ai/docs/custom-tools)
- [Plugins](https://opencode.ai/docs/plugins)
- [Server](https://opencode.ai/docs/server/)
- [SDK](https://opencode.ai/docs/sdk/)
- [oh-my-opencode-slim](https://github.com/alvinunreal/oh-my-opencode-slim)
- [Spring Boot 文档](https://docs.spring.io/spring-boot/)
- [Spring Boot 系统要求](https://docs.spring.io/spring-boot/system-requirements.html)
