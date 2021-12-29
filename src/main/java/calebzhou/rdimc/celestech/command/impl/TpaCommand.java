package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.enums.ColorConst;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

import java.util.UUID;

public class TpaCommand extends BaseCommand {

    public TpaCommand(String command, int permissionLevel) {
        super(command, permissionLevel);
    }
    public static final SuggestionProvider<ServerCommandSource> SUGGESTION_PROVIDER = (context, builder) -> CommandSource.suggestMatching(ServerUtils.getOnlinePlayerList(), builder);
    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.then(
                CommandManager.argument("targetPlayer", EntityArgumentType.players())
                        .suggests(SUGGESTION_PROVIDER)
                        .executes(context ->
                                execute(context.getSource(), EntityArgumentType.getPlayer(context, "targetPlayer"))
                        )
        );
    }

    private int execute(ServerCommandSource source, ServerPlayerEntity toPlayer) throws CommandSyntaxException {
        ServerPlayerEntity fromPlayer = source.getPlayer();
        String fromPlayerName = fromPlayer.getDisplayName().getString();
        String toPlayerName = toPlayer.getDisplayName().getString();
        if(fromPlayer==toPlayer){
            TextUtils.sendChatMessage(fromPlayer,"禁止原地TP");
            return Command.SINGLE_SUCCESS;
        }
        if(RDICeleTech.tpaRequestMap.containsKey(fromPlayer.getUuidAsString())){
            TextUtils.sendChatMessage(fromPlayer,"您已经给发送过传送请求了");
            TextUtils.sendClickableContent(fromPlayer,"点击此处可以删除您发送过的所有传送请求。","tpaclear");
        }

        TextUtils.sendChatMessage(fromPlayer,"传送请求已发送给"+toPlayerName);

        String reqid= UUID.randomUUID().toString().substring(1,8);
        PlayerTpaRequest preq=new PlayerTpaRequest(fromPlayer,toPlayer);
        RDICeleTech.tpaRequestMap.put(reqid,preq);
        TextUtils.sendChatMessage(fromPlayer,"已经发送传送请求给"+toPlayerName+", 请求ID:"+reqid);
        TextUtils.sendChatMessage(toPlayer, ColorConst.ORANGE+fromPlayerName+"想要传送到你的身边。");
        TextUtils.sendChatMessage(toPlayer, ColorConst.ORANGE+"为防止恶意破坏，请谨慎接受传送请求。");
        MutableText tpyes=TextUtils.getClickableContentComp(ColorConst.BRIGHT_GREEN+"[接受]"+ ColorConst.RESET,"/tpyes "+reqid," ");
        MutableText tpwait=TextUtils.getClickableContentComp(ColorConst.GOLD+"[等我一下]"+ ColorConst.RESET,"稍等"," ");
        TextUtils.sendChatMessage(toPlayer,tpyes.append(tpwait));

        return Command.SINGLE_SUCCESS;
    }

    public class PlayerTpaRequest {
        public PlayerTpaRequest(PlayerEntity fromPlayer, PlayerEntity toPlayer) {
            this.fromPlayer = fromPlayer;
            this.toPlayer = toPlayer;
        }
        private PlayerEntity fromPlayer;
        private PlayerEntity toPlayer;

        public PlayerEntity getFromPlayer() {
            return fromPlayer;
        }

        public PlayerEntity getToPlayer() {
            return toPlayer;
        }
    }
}
