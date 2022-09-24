package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdi.core.server.utils.PlayerUtils.*;

/**
 * Created by calebzhou on 2022-09-22,11:17.
 */
public class WeatherCommand extends RdiCommand {
	public WeatherCommand() {
		super("change-weather","花费3经验改变天气。");
	}

	@Override
	public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
		return baseArgBuilder
				.then(
						Commands.literal("clear")
								.executes(context -> setWeather(context.getSource().getPlayer(), 6000,0,false,false))

				)
				.then(
						Commands.literal("rain")
								.executes(context -> setWeather(context.getSource().getPlayer(), 0,6000,true,false))

				)
				.then(
						Commands.literal("thunder")
								.executes(context -> setWeather(context.getSource().getPlayer(), 0,6000,true,true))

				);
	}
	private static int setWeather(ServerPlayer player, int clearTime,int rainTime,boolean rain,boolean thunder ){
		if(player.experienceLevel<3){
			sendChatMessage(player, RESPONSE_ERROR,"改变天气需要3经验！");
			return 1;
		}
		if(!isInIsland(player)){
			sendChatMessage(player, RESPONSE_ERROR,"必须在自己的岛上！");
			return 1;
		}
		player.getLevel().setWeatherParameters(clearTime, rainTime, rain, thunder);
		sendChatMessage(player, RESPONSE_SUCCESS);
		player.setExperienceLevels(player.experienceLevel-3);
		return 1;
	}
	/*private static int setClear(CommandSourceStack source, int time) {
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
