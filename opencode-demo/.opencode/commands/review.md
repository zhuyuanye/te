---
description: 只读审查指定范围和当前 Git 差异
agent: reviewer
subtask: true
---

请审查以下范围：

$ARGUMENTS

当前仓库状态：

!`git status --short`

当前差异摘要：

!`git diff --stat`

当前补丁：

!`git diff -- .`

只报告实质性的正确性、安全性、兼容性、性能或回归问题，不要修改文件。
