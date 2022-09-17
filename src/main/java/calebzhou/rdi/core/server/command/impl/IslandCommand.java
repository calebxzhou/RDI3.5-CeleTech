package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.thread.RdiHttpPlayerRequest;
import calebzhou.rdi.core.server.thread.RdiHttpRequest;
import calebzhou.rdi.core.server.thread.RdiRequestThread;
import calebzhou.rdi.core.server.utils.*;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import xyz.nucleoid.fantasy.Fantasy;
import xyz.nucleoid.fantasy.RuntimeWorldHandle;

import static calebzhou.rdi.core.server.utils.IslandUtils.getIslandDimensionLoca;
import static calebzhou.rdi.core.server.utils.PlayerUtils.sendChatMessage;

public class IslandCommand extends RdiCommand {
    public IslandCommand() {
        super("is");
    }
    static final String islandHelp = """
            =====RDI空岛v2管理菜单=====
            指令参数
            create 创建岛屿 reset 重置岛屿 kick 移除成员 invite 邀请成员 loca 更改传送点
            transfer 改变岛主 quit 退出加入的岛屿
            ====================
            """;


    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> sendIslandHelp(context.getSource().getPlayer()))
                .then(
                        Commands.argument("指令参数", StringArgumentType.string())
                                .suggests(
                                        (context, builder) ->
                                                SharedSuggestionProvider.suggest(new String[]{
                                                        "create","reset","kick","invite","loca","transfer","quit"},builder)
                                )
                                .executes(
                                        context -> handleSubCommand(context.getSource().getPlayer(),StringArgumentType.getString(context,"指令参数"))
                                )
                                .then(
                                        Commands.argument("玩家名", EntityArgument.player())
                                                .executes(context -> handleSubCommandWithPlayerNameParam(StringArgumentType.getString(context,"指令参数"),context.getSource().getPlayer(),EntityArgument.getPlayer(context,"玩家名")))
                                )
                )
                ;
    }
    private int handleSubCommandWithPlayerNameParam(String param, ServerPlayer fromPlayer, ServerPlayer toPlayer) {
        switch (param){
            case "invite" -> invitePlayer(fromPlayer,toPlayer);
            case "kick" -> kickPlayer(fromPlayer,toPlayer);
            case "transfer" -> confirmedTransferToPlayer(fromPlayer,toPlayer);
            default -> sendIslandHelp(fromPlayer);
        }
        return 1;
    }



    private int handleSubCommand(ServerPlayer player, String param) {
        switch (param){
            case "create" -> createIsland(player);
            case "reset" -> confirmedResetIsland(player);
            case "loca" -> locateIsland(player);
            case "quit" -> confirmedQuitIsland(player);
            default -> sendIslandHelp(player);
        }
        return 1;
    }

    private void confirmedQuitIsland(ServerPlayer player) {
        RdiRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.delete,
                player,
                msg->{
                    if(msg.startsWith("-")){
                        sendChatMessage(player, PlayerUtils.RESPONSE_ERROR,msg);
                        return;
                    }
                    ServerUtils.executeOnServerThread(()-> {
                        PlayerUtils.resetProfile(player);
                        sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS, msg);
                    });
                },
                "island2/crew/"+player.getStringUUID()
        ));
    }
    private void confirmedTransferToPlayer(ServerPlayer fromPlayer, ServerPlayer toPlayer) {
        if(fromPlayer==toPlayer){
            sendChatMessage(fromPlayer,PlayerUtils.RESPONSE_ERROR,"目标玩家不能是自己！");
            return;
        }
        RdiRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.put,
                fromPlayer,
                msg->{
                    if(msg.startsWith("-")){
                        sendChatMessage(fromPlayer, PlayerUtils.RESPONSE_ERROR,msg);
                        return;
                    }
                    sendChatMessage(fromPlayer,PlayerUtils.RESPONSE_SUCCESS,msg);
                    sendChatMessage(toPlayer,PlayerUtils.RESPONSE_SUCCESS, fromPlayer.getScoreboardName()+"把岛屿转让给了你！");
                },
                "island2/transfer/"+fromPlayer.getStringUUID()+"/"+toPlayer.getStringUUID()
        ));
    }

    private void createIsland(ServerPlayer player){
        sendChatMessage(player, PlayerUtils.RESPONSE_INFO,"准备创建岛屿，不要触碰鼠标或者键盘！");
        RdiRequestThread.addTask(new RdiHttpPlayerRequest(
                        RdiHttpRequest.Type.post,
                        player,
                        msg->{
                            if(msg.startsWith("-")){
                                sendChatMessage(player, PlayerUtils.RESPONSE_ERROR,msg);
                                return;
                            }
                            sendChatMessage(player, PlayerUtils.RESPONSE_INFO,"开始创建岛屿，不要触碰鼠标或者键盘！");
                            int iid =Integer.parseInt(msg);
                            sendChatMessage(player, PlayerUtils.RESPONSE_INFO,"您的岛屿ID："+iid);
                            MinecraftServer server = RdiCoreServer.getServer();
                            ServerUtils.executeOnServerThread(()->{
                                Fantasy fantasy = Fantasy.get(server);
                                sendChatMessage(player, PlayerUtils.RESPONSE_INFO,"正在创建存档。。");
                                ResourceLocation islandDimension = getIslandDimensionLoca(iid);
                                RuntimeWorldHandle worldHandle = fantasy.getOrOpenPersistentWorld(islandDimension, IslandUtils.getIslandWorldConfig());
                                sendChatMessage(player, PlayerUtils.RESPONSE_INFO,"正在载入地形。。");
                                ServerLevel level = worldHandle.asWorld();
                                WorldUtils.placeInitialBlocks(level);
                                sendChatMessage(player, PlayerUtils.RESPONSE_INFO,"放置方块中。。");
                                PlayerUtils.addEffect(player, MobEffects.SLOW_FALLING,10,2);
                                PlayerUtils.setSpawnPoint(player,level.dimension(),WorldUtils.INIT_POS.above(7));
                                sendChatMessage(player, PlayerUtils.RESPONSE_INFO,"准备传送。。。");
                                PlayerUtils.teleport(player,level,WorldUtils.INIT_POS.above(7));
                                sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS,"成功！");
                            });

                        },
                        "island2/"+ player.getStringUUID()

                )
        );
    }
    private void locateIsland(ServerPlayer player) {
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        double w = player.getXRot();
        double p = player.getYRot();
        String dim = WorldUtils.getDimensionName(player.level);
        RdiRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.put,
                player,
                resp-> {
                    if(resp.startsWith("-")){
                        sendChatMessage(player,PlayerUtils.RESPONSE_ERROR,resp);
                        return;
                    }
                    sendChatMessage(player,PlayerUtils.RESPONSE_SUCCESS,resp+"成功将您的空岛传送点改成了%f,%f,%f".formatted(x,y,z));
                },
                "island2/loca/"+player.getStringUUID(),
                "x="+x,"y="+y,"z="+z,"w="+w,"p="+p,"dim="+dim
        ));

    }
    private void kickPlayer(ServerPlayer fromPlayer, ServerPlayer kickPlayer) {
        if(fromPlayer==kickPlayer){
            sendChatMessage(fromPlayer, PlayerUtils.RESPONSE_ERROR,"您不可以踢出自己!!");
            return;
        }
        RdiRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.delete,
                fromPlayer,
                response->{
                    if(response.equals("1")) {
                        sendChatMessage(kickPlayer, fromPlayer.getScoreboardName() + "删除了他的岛屿!");
                        PlayerUtils.resetProfile(kickPlayer);
                        sendChatMessage(fromPlayer, PlayerUtils.RESPONSE_SUCCESS, "1");
                    }else
                        sendChatMessage(fromPlayer,PlayerUtils.RESPONSE_ERROR,response);
                },
                "island2/crew/" +  kickPlayer.getStringUUID()
        ));
    }

    private void invitePlayer(ServerPlayer player, ServerPlayer invitedPlayer) {
        if(player==invitedPlayer){
            sendChatMessage(player,PlayerUtils.RESPONSE_ERROR,"目标玩家不能是自己！");
            return;
        }
        RdiRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.post,
                player,
                response->{

                    sendChatMessage(player,"您邀请了"+invitedPlayer);
                    if(response.equals("1")){
                        sendChatMessage(invitedPlayer,PlayerUtils.RESPONSE_INFO,player.getScoreboardName()+"邀请您加入了他的岛屿");
                        TextUtils.sendClickableContent(invitedPlayer,"按下[H键]可以前往他的岛屿","/home");
                        sendChatMessage(player,PlayerUtils.RESPONSE_SUCCESS,"1");
                    }else
                        sendChatMessage(player,PlayerUtils.RESPONSE_ERROR,response);
                },
                "island2/crew/" + player.getStringUUID()+"/"+invitedPlayer.getStringUUID()
        ));
    }

    private void confirmedResetIsland(ServerPlayer player) {
        RdiRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.delete,
                player,
                resp->{
                    if(resp.startsWith("-")){
                        sendChatMessage(player,PlayerUtils.RESPONSE_ERROR,resp);
                        return;
                    }
                    ResourceLocation dim = IslandUtils.getIslandDimensionLoca(resp);
                    ServerUtils.executeOnServerThread(()-> {
                        RuntimeWorldHandle worldHandle = Fantasy.get(RdiCoreServer.getServer()).getOrOpenPersistentWorld(dim, IslandUtils.getIslandWorldConfig());
                        PlayerUtils.resetProfile(player);
                        worldHandle.delete();
                        sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS, resp);
                    });
                },
                "island2/" + player.getStringUUID()
        ));
    }

    private int sendIslandHelp(ServerPlayer player) {
        TextUtils.sendChatMultilineMessage(player,islandHelp);
        return 1;
    }

}
