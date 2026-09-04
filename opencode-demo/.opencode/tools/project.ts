import { tool } from "@opencode-ai/plugin"

export default tool({
  description: "返回当前 OpenCode 会话和项目的非敏感上下文",
  args: {},
  async execute(_args, context) {
    const { agent, sessionID, messageID, directory, worktree } = context

    return JSON.stringify(
      {
        agent,
        sessionID,
        messageID,
        directory,
        worktree,
      },
      null,
      2,
    )
  },
})
