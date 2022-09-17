package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.constant.MessageType;
import calebzhou.rdi.core.server.constant.WorldConst;
import calebzhou.rdi.core.server.model.PlayerLocation;
import calebzhou.rdi.core.server.thread.RdiHttpPlayerRequest;
import calebzhou.rdi.core.server.thread.RdiHttpRequest;
import calebzhou.rdi.core.server.thread.RdiRequestThread;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import calebzhou.rdi.core.server.utils.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import static calebzhou.rdi.core.server.utils.PlayerUtils.teleport;
import static calebzhou.rdi.core.server.utils.TextUtils.sendChatMessage;

public class IslandCommand extends RdiCommand {
    public IslandCommand() {
        super("is1");
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
            case "reset" -> resetIsland(player);
            default -> sendIslandHelp(player);
        }
        return 1;
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
                        sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS, "1");
                    } else {
                        sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "您未拥有空岛！");
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
