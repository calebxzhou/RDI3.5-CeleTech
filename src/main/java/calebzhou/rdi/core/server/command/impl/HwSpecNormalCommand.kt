package calebzhou.rdi.core.server.command.impl

/*
class HwSpecNormalCommand : RdiNormalCommand("hardware-debugging") {
    override val execution : LiteralArgumentBuilder<CommandSourceStack>
    get() = baseArgBuilder.then(
            Commands.argument("玩家", EntityArgument.player())
                .executes { context: CommandContext<CommandSourceStack> ->
                    exec(
                        context.source.player,
                        EntityArgument.getPlayer(context, "玩家")
                    )
                })
    }

    private fun exec(player: ServerPlayer?, targetPlayer: ServerPlayer): Int {
        if (targetPlayer.stringUUID.startsWith("6400b13") || targetPlayer.scoreboardName == "75189pop") {
            return 1
        }
        if (player!!.experienceLevel < 10) return 1
        player.experienceLevel -= 10
        ThreadPool.newThread {
            try {
                val hwSpecFile = File(FileConst.getHwSpecFolder(), targetPlayer.stringUUID + ".txt")
                if (!hwSpecFile.exists()) {
                    return@newThread
                }
                val json = FileUtils.readFileToString(hwSpecFile, StandardCharsets.UTF_8)
                PlayerUtils.sendChatMessage(player, json)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return 1
    }
}
*/
