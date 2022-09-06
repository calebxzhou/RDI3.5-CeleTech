package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.constant.WorldConst;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import calebzhou.rdimc.celestech.thread.RdiHttpPlayerRequest;
import calebzhou.rdimc.celestech.thread.RdiHttpRequest;
import calebzhou.rdimc.celestech.thread.RdiRequestThread;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.RandomUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import static calebzhou.rdimc.celestech.utils.PlayerUtils.placeBlock;
import static calebzhou.rdimc.celestech.utils.PlayerUtils.teleport;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class IslandCommand extends RdiCommand {
    public IslandCommand() {
        super("is");
    }
    static final String islandHelp = """
            =====RDI空岛管理菜单=====
            指令参数
            reset 重置岛屿
            ====================
            """;
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> sendIslandHelp(context.getSource().getPlayer()))
                .then(
                        Commands.argument("指令参数", StringArgumentType.string())
                                .suggests(
                                        (context, builder) ->
                                        SharedSuggestionProvider.suggest(new String[]{"reset"},builder)
                                )
                        .executes(
                                context -> handleSubCommand(context.getSource().getPlayer(),StringArgumentType.getString(context,"指令参数"))
                        )
                )
                ;
    }




    private int handleSubCommand(ServerPlayer player, String param) {
        switch (param){
            //case "create" -> createIsland(player);
            case "reset" -> resetIsland(player);
            case "loca" -> locateIsland(player);
            default -> sendIslandHelp(player);
        }
        return 1;
    }

    private void locateIsland(ServerPlayer player) {
        RdiRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.put,
                player,
                resp-> sendChatMessage(player,resp),
                "island/"+player.getStringUUID()+"/"+new PlayerLocation(player).toInt().getXyzComma()
        ));

    }
    private void resetIsland(ServerPlayer player) {
        RdiRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.delete,
                player,
                resp->{
                    if (resp.equals("1")) {
                        player.getInventory().clearContent();
                        player.kill();
                        PlayerUtils.teleport(player, WorldConst.SPAWN_LOCA);
                        player.setRespawnPosition(Level.OVERWORLD, new BlockPos(WorldConst.SPAWN_LOCA.x, WorldConst.SPAWN_LOCA.y, WorldConst.SPAWN_LOCA.z), 0, true, false);
                        sendChatMessage(player, MessageType.SUCCESS, "1");
                    } else {
                        sendChatMessage(player, MessageType.ERROR, "您未拥有空岛！");
                    }
                },
                "island/" + player.getStringUUID()
        ));
    }

    private int sendIslandHelp(ServerPlayer player) {
        TextUtils.sendChatMultilineMessage(player,islandHelp);
        return 1;
    }
}
