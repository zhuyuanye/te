# 原生 OpenCode Agent 模型配置示例

本目录用于分享和讲解，不会被 OpenCode 自动加载。

`architect.md` 展示如何为项目级自定义 Agent 单独指定模型和推理强度。实际使用前先运行：

```bash
opencode models --refresh
opencode models
```

然后确认示例中的 `openai/gpt-5.6-sol` 确实存在；如果不存在，请替换成当前账户返回的 `provider/model-id`。

## 推荐的角色分配

下面是模型能力的分配思路，不要求必须使用同一家供应商：

| Agent | 模型选择 | 推理强度 |
| --- | --- | --- |
| `architect` | 强推理、架构判断稳定 | `high` |
| `reviewer` | 强推理、擅长发现边界问题 | `high` |
| `debugger` | 强推理、工具调用可靠 | `high` |
| `implementer` | 编码能力强、速度和成本均衡 | `medium` 或 `high` |
| `docs` | 快速、低成本、中文表达稳定 | `low` 或 `medium` |

## OpenCode 1.x 写法

```yaml
model: openai/gpt-5.6-sol
reasoningEffort: high
```

`reasoningEffort` 属于模型供应商选项。不同模型支持的值可能不同，不能假定所有模型都支持 `low`、`medium` 或 `high`。

## OpenCode V2 写法

V2 将 Variant 拼接到模型引用中：

```yaml
model: openai/gpt-5.6-sol#high
```

不要在同一个 Agent 文件里同时使用两种写法。

## 使用方式

确认模型可用后，可以把示例复制到项目 Agent 目录：

```bash
cp config-snippets/agent-model-routing/architect.md .opencode/agents/architect.md
```

该操作会覆盖现有 `architect.md`，正式使用时应先比较差异或手工添加 `model` 与 `reasoningEffort` 两行。
