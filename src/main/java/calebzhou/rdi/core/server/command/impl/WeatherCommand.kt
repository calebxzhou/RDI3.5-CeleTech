package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.command.RdiCommand
import calebzhou.rdi.core.server.utils.PlayerUtils
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.level.ServerPlayer

/**
 * Created by calebzhou on 2022-09-22,11:17.
 */
class WeatherCommand : RdiCommand("change-weather", "花费3经验改变天气。") {
    override fun getExecution(): LiteralArgumentBuilder<CommandSourceStack> {
        return baseArgBuilder
            .then(
                Commands.literal("clear")
                    .executes { context: CommandContext<CommandSourceStack> ->
                        setWeather(
                            context.source.player,
                            6000,
                            0,
                            false,
                            false
                        )
                    }
            )
            .then(
                Commands.literal("rain")
                    .executes { context: CommandContext<CommandSourceStack> ->
                        setWeather(
                            context.source.player,
                            0,
                            6000,
                            true,
                            false
                        )
                    }
            )
            .then(
                Commands.literal("thunder")
                    .executes { context: CommandContext<CommandSourceStack> ->
                        setWeather(
                            context.source.player,
                            0,
                            6000,
                            true,
                            true
                        )
                    }
            )
    }

    companion object {
        private fun setWeather(
            player: ServerPlayer?,
            clearTime: Int,
            rainTime: Int,
            rain: Boolean,
            thunder: Boolean
        ): Int {
            if (player!!.experienceLevel < 3) {
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "改变天气需要3经验！")
                return 1
            }
            if (!PlayerUtils.isInIsland(player)) {
                PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "必须在自己的岛上！")
                return 1
            }
            player.getLevel().setWeatherParameters(clearTime, rainTime, rain, thunder)
            PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS)
            player.setExperienceLevels(player.experienceLevel - 3)
            return 1
        } /*private static int setClear(CommandSourceStack source, int time) {
		source.getLevel().setWeatherParameters(time, 0, false, false);
		source.sendSuccess(Component.translatable("commands.weather.set.clear"), true);
		return time;
	}

	private static int setRain(CommandSourceStack source, int time) {
		source.getLevel().setWeatherParameters(0, time, true, false);
		source.sendSuccess(Component.translatable("commands.weather.set.rain"), true);
		return time;
	}

	private static int setThunder(CommandSourceStack source, int time) {
		source.getLevel().setWeatherParameters(0, time, true, true);
		source.sendSuccess(Component.translatable("commands.weather.set.thunder"), true);
		return time;
	}*/
    }
}
