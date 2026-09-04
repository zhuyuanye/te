export const EnvProtectionPlugin = async () => {
  return {
    "tool.execute.before": async (input, output) => {
      if (input.tool !== "read") return

      const filePath = String(output.args.filePath ?? output.args.path ?? "")
      const fileName = filePath.split(/[\\/]/).at(-1) ?? ""

      if (fileName === ".env" || fileName.startsWith(".env.")) {
        throw new Error("演示安全插件已阻止读取环境变量文件")
      }
    },
  }
}
