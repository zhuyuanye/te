import type { Plugin } from "@opencode-ai/plugin"

export const DemoLoggerPlugin: Plugin = async ({ client, directory }) => {
  await client.app.log({
    body: {
      service: "opencode-advanced-demo",
      level: "info",
      message: "演示插件已初始化",
      extra: { directory },
    },
  })

  return {
    event: async ({ event }) => {
      if (event.type !== "session.idle") return

      await client.app.log({
        body: {
          service: "opencode-advanced-demo",
          level: "info",
          message: "会话已进入空闲状态",
        },
      })
    },
  }
}
