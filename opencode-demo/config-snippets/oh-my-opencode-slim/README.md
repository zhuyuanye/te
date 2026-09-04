# oh-my-opencode-slim 配置示例

本目录只用于分享和讲解，不会被 OpenCode 自动加载。

- `oh-my-opencode-slim.jsonc`：团队模型预设示例。实际使用时复制到 `~/.config/opencode/oh-my-opencode-slim.jsonc`，并根据 `opencode models --refresh` 的结果调整模型。
- `orchestrator_append.md`：项目级中文补充 Prompt 示例。实际使用时复制到 `.opencode/oh-my-opencode-slim/orchestrator_append.md`。

安装插件前先执行：

```bash
bunx oh-my-opencode-slim@latest install --dry-run
```

插件属于第三方依赖。正式引入前应审查安装行为、固定版本，并在非关键仓库完成验证。
