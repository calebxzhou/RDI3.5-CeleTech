package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.NetworkPackets
import calebzhou.rdi.core.server.command.RdiCommand
import calebzhou.rdi.core.server.utils.NetworkUtils
import calebzhou.rdi.core.server.utils.PlayerUtils
import calebzhou.rdi.core.server.utils.RdiHttpClient
import calebzhou.rdi.core.server.utils.ThreadPool
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import it.unimi.dsi.fastutil.Pair
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

class SetPasswordCommand : RdiCommand("set-password", "设定密码") {
    override fun getExecution(): LiteralArgumentBuilder<CommandSourceStack> {
        return baseArgBuilder
            .then(
                Commands.argument("用来加密的密码", StringArgumentType.string())
                    .then(
                        Commands.argument("确认要加密的密码", StringArgumentType.string())
                            .executes { context: CommandContext<CommandSourceStack> -> exec(context) }
                    )
            )
    }

    private fun exec(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.player
        val pwdSet = StringArgumentType.getString(context, "用来加密的密码")
        val pwdVerify = StringArgumentType.getString(context, "确认要加密的密码")
        if (pwdSet != pwdVerify) {
            PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "两次密码输入不一致！")
            return 1
        }
        if (pwdVerify.length < 6 || pwdVerify.length > 16) {
            PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "密码最少6位，最多16位！")
            return 1
        }
        ThreadPool.newThread {
            val resultData = RdiHttpClient.sendRequest(
                "post",
                "/v37/account/register/" + player!!.stringUUID,
                Pair.of("pwd", pwdVerify),
                Pair.of("ip", player.ipAddress)
            )
            if (resultData.isSuccess) {
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS, "加密成功，请牢记密码：$pwdVerify")
                PlayerUtils.sendChatMessage(
                    player,
                    PlayerUtils.RESPONSE_SUCCESS,
                    "密码存储位置：" + PlayerUtils.getPasswordStorageFile(player)
                )
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS, "启动游戏时将自动读取密码，不需要您手动输入")
                NetworkUtils.sendPacketToClient(player, NetworkPackets.SET_PASSWORD, pwdVerify)
            } else PlayerUtils.sendServiceResultData(player, resultData)
        }
        return 1
    }
}
