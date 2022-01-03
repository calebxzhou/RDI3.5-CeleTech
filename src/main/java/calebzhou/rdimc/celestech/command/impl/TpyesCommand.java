package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static calebzhou.rdimc.celestech.utils.TextUtils.*;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendClickableContent;

public class TpyesCommand extends BaseCommand {

    public TpyesCommand(String command, int permissionLevel) {
        super(command, permissionLevel);
    }
    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.then(
                CommandManager.argument("req", StringArgumentType.string())
                        .executes(context ->
                                execute(context.getSource(), StringArgumentType.getString(context, "req"))
                        )
        );
    }

    private int execute(ServerCommandSource source, String reqid) throws CommandSyntaxException {
        ServerPlayerEntity toPlayer = source.getPlayer();
        if(toPlayer.experienceLevel<3){
            sendChatMessage(toPlayer,"经验不足！");
            return 1;
        }
        toPlayer.experienceLevel -= 3;
        TpaCommand.PlayerTpaRequest tpaRequest= RDICeleTech.tpaRequestMap.get(reqid);
        if(tpaRequest==null){
            sendChatMessage(toPlayer,"没有找到此请求。请确认1.对方是否在线2.是否已经接受了此请求");
            sendChatMessage(toPlayer,"请求ID:"+reqid);
            return 1;
        }
        sendChatMessage(toPlayer,"正在传送..");
        sendChatMessage(tpaRequest.getFromPlayer(),"正在传送..");
        PlayerUtils.teleportPlayer(tpaRequest.getFromPlayer(),toPlayer);
        RDICeleTech.tpaRequestMap.remove(reqid);
        return Command.SINGLE_SUCCESS;
    }
}
