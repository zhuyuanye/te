# 自动化脚本

## 无界面审查

```bash
./scripts/headless-review.sh
```

脚本使用 `--format json` 输出原始事件，适合后续交给 `jq` 或 CI 系统处理。

## 连接常驻服务

先在一个终端运行：

```bash
opencode serve
```

然后在另一个终端运行：

```bash
./scripts/server-review.sh
```

连接常驻服务适合连续执行多个任务，并能避免每次启动 MCP Server 的冷启动成本。
