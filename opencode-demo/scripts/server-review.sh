#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

opencode run \
  --attach http://localhost:4096 \
  --agent reviewer \
  "审查 PricingService、PricingController 及其测试，不要修改文件。"
