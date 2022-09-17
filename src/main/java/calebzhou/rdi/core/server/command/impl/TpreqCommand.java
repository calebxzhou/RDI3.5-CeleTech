package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiMemoryStorage;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

import static calebzhou.rdi.core.server.utils.TextUtils.sendChatMessage;
import static calebzhou.rdi.core.server.utils.TextUtils.sendClickableContent;

public class TpreqCommand extends RdiCommand {
    public TpreqCommand(   ) {
        super("tpreq");
    }


    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.then(Commands.argument("target", StringArgumentType.string())
                .executes(context -> exec(context.getSource().getPlayer(), StringArgumentType.getString(context, "target"))));
    }

    private int exec(ServerPlayer toPlayer, String arg) {
        try {
            String[] split = arg.split("_");
            boolean accept = Boolean.parseBoolean(split[0]);
            boolean visitOnly = Boolean.parseBoolean(split[1]);
            String fromPlayerId = split[2];
            ServerPlayer fromPlayer = PlayerUtils.getPlayerByUuid(fromPlayerId);
            if (toPlayer == null) {
                sendChatMessage(fromPlayer, PlayerUtils.RESPONSE_ERROR, fromPlayer.getScoreboardName() + "不在线，您无法传送到对方。");
                return 1;
            }
            execute(toPlayer, accept, visitOnly, fromPlayer);
        } catch (ArrayIndexOutOfBoundsException e) {
            sendChatMessage(toPlayer, PlayerUtils.RESPONSE_ERROR, "命令格式错误!!");
        }
        return 1;
    }

    private void execute(ServerPlayer toPlayer, boolean accept, boolean visitOnly, ServerPlayer fromPlayer) {
        String fromPlayerId = fromPlayer.getStringUUID();
        if(RdiMemoryStorage.tpaMap.get(fromPlayerId) == null){
            sendChatMessage(toPlayer,"没有找到此请求。请确认1.对方是否在线2.是否已经接受了此请求");
            return;
        }
        if(!accept){
            sendChatMessage(fromPlayer,PlayerUtils.RESPONSE_ERROR,"对方拒绝了您的传送请求");
            RdiMemoryStorage.tpaMap.remove(fromPlayerId);
            return;
        }
        if(toPlayer.experienceLevel<3){
            sendChatMessage(toPlayer,PlayerUtils.RESPONSE_ERROR,"经验不足,您需要3级经验.");
            return;
        }
        toPlayer.experienceLevel -= 3;

        sendChatMessage(toPlayer,"正在传送..");
        sendChatMessage(fromPlayer,"正在传送..");
        PlayerUtils.teleport(fromPlayer,toPlayer);
        if(visitOnly){
            fromPlayer.gameMode.changeGameModeForPlayer(GameType.SPECTATOR);
            sendChatMessage(fromPlayer,"对方使用了'仅参观'模式接受了你的传送请求");
            sendClickableContent(fromPlayer,"若要恢复,点击这里.","/spawn");
        }

        RdiMemoryStorage.tpaMap.remove(fromPlayerId);
    }


}
