import { tool } from "@opencode-ai/plugin"

export const add = tool({
  description: "将两个有限数字相加，用于演示 OpenCode 自定义工具",
  args: {
    a: tool.schema.number().finite().describe("第一个数字"),
    b: tool.schema.number().finite().describe("第二个数字"),
  },
  async execute(args) {
    return String(args.a + args.b)
  },
})

export const multiply = tool({
  description: "将两个有限数字相乘，用于演示 OpenCode 自定义工具",
  args: {
    a: tool.schema.number().finite().describe("第一个数字"),
    b: tool.schema.number().finite().describe("第二个数字"),
  },
  async execute(args) {
    return String(args.a * args.b)
  },
})
