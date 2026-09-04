#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

opencode run \
  --format json \
  --agent reviewer \
  "审查当前 Spring Boot 项目是否存在实质性正确性问题，不要修改文件。"
