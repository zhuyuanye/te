import { tool } from "@opencode-ai/plugin"

const MUTATING_SQL = /\b(insert|update|delete|drop|alter|truncate|create|grant|revoke)\b/i

export default tool({
  description: "校验用于演示的只读 SQL；该工具不会连接真实数据库",
  args: {
    query: tool.schema.string().min(1).describe("一条只读 SELECT 查询"),
  },
  async execute({ query }) {
    const normalized = query.trim()

    if (!/^select\b/i.test(normalized) || MUTATING_SQL.test(normalized)) {
      return "已拒绝：演示工具只接受只读 SELECT 语句。"
    }

    return JSON.stringify(
      {
        executed: false,
        reason: "演示模式没有数据库连接",
        validatedQuery: normalized,
      },
      null,
      2,
    )
  },
})
