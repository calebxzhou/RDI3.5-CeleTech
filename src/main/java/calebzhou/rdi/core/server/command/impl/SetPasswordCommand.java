package calebzhou.rdi.core.server.command.impl;


import calebzhou.rdi.core.server.NetworkPackets;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.model.ResultData;
import calebzhou.rdi.core.server.utils.NetworkUtils;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import calebzhou.rdi.core.server.utils.RdiHttpClient;
import calebzhou.rdi.core.server.utils.ThreadPool;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdi.core.server.utils.PlayerUtils.*;

public class SetPasswordCommand extends RdiCommand {

	public SetPasswordCommand() {
        super( "set-password","设定密码");
    }



    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder
                .then(Commands.argument("用来加密的密码", StringArgumentType.string())
                        .then(Commands.argument("确认要加密的密码",StringArgumentType.string())
                                .executes(this::exec)
                        )
                );
    }

	private int exec(CommandContext<CommandSourceStack> context) {
		ServerPlayer player = context.getSource().getPlayer();
		String pwdSet = StringArgumentType.getString(context, "用来加密的密码");
		String pwdVerify = StringArgumentType.getString(context, "确认要加密的密码");
		if(!pwdSet.equals(pwdVerify)){
			sendChatMessage(player, RESPONSE_ERROR,"两次密码输入不一致！");
			return 1;
		}
		if(pwdVerify.length()<6||pwdVerify.length()>16){
			sendChatMessage(player, RESPONSE_ERROR,"密码最少6位，最多16位！");
			return 1;
		}
		ThreadPool.newThread(()->{
			ResultData resultData = RdiHttpClient.sendRequest("post", "/v37/account/register/" + player.getStringUUID() , Pair.of("pwd",pwdVerify),Pair.of("ip",player.getIpAddress()));
			if(resultData.isSuccess()){
				sendChatMessage(player, RESPONSE_SUCCESS,"加密成功，请牢记密码："+pwdVerify);
				sendChatMessage(player, RESPONSE_SUCCESS,"密码存储位置："+getPasswordStorageFile(player));
				sendChatMessage(player, RESPONSE_SUCCESS,"启动游戏时将自动读取密码，不需要您手动输入");
				NetworkUtils.sendPacketToClient(player, NetworkPackets.SET_PASSWORD,pwdVerify);
			}else PlayerUtils.sendServiceResultData(player,resultData);
		});
		return 1;
	}

}
