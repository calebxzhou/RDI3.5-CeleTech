package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.RdiSharedConstants;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.constant.WorldConst;
import calebzhou.rdimc.celestech.thread.RdiHttpPlayerRequest;
import calebzhou.rdimc.celestech.thread.RdiHttpRequest;
import calebzhou.rdimc.celestech.thread.RdiIslandRequestThread;
import calebzhou.rdimc.celestech.utils.*;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import xyz.nucleoid.fantasy.Fantasy;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;
import xyz.nucleoid.fantasy.RuntimeWorldHandle;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class Island2Command extends RdiCommand {
    public Island2Command() {
        super("is2");
    }
    static final String islandHelp = """
            =====RDI空岛v2管理菜单=====
            指令参数
            create 创建岛屿 reset 重置岛屿 kick 移除成员 invite 邀请成员 loca 更改传送点
            transfer 改变岛主 quit 退出加入的岛屿
            
            ====================
            """;
    public static RuntimeWorldConfig getIslandWorldConfig(){
        return new RuntimeWorldConfig()
                .setDimensionType(BuiltinDimensionTypes.OVERWORLD)
                .setDifficulty(Difficulty.HARD)
                .setGenerator(RDICeleTech.getServer().overworld().getChunkSource().getGenerator())
                .setSeed(System.currentTimeMillis());
    }
    public static ResourceLocation getIslandDimensionLoca(int iid){
        return getIslandDimensionLoca(iid + "");
    }
    public static ResourceLocation getIslandDimensionLoca(String iid){
        return new ResourceLocation(RdiSharedConstants.MOD_ID, RdiSharedConstants.ISLAND_DIMENSION_PREFIX + iid);
    }

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
            case "transfer" -> transferToPlayer(fromPlayer,toPlayer);
            default -> sendIslandHelp(fromPlayer);
        }
        return 1;
    }



    private int handleSubCommand(ServerPlayer player, String param) {
        switch (param){
            case "create" -> createIsland(player);
            case "reset" -> resetIsland(player);
            case "loca" -> locateIsland(player);
            case "quit" -> quitIsland(player);
            default -> sendIslandHelp(player);
        }
        return 1;
    }

    private void quitIsland(ServerPlayer player) {
        RdiIslandRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.delete,
                player,
                msg->{
                    if(msg.startsWith("-")){
                        sendChatMessage(player, MessageType.ERROR,msg);
                        return;
                    }
                    ServerUtils.executeOnServerThread(()-> {
                        PlayerUtils.resetProfile(player);
                        sendChatMessage(player, MessageType.SUCCESS, msg);
                    });
                },
                "island2/crew/"+player.getStringUUID()
        ));
    }
    private void transferToPlayer(ServerPlayer fromPlayer, ServerPlayer toPlayer) {
        if(fromPlayer==toPlayer){
            TextUtils.sendChatMessage(fromPlayer,MessageType.ERROR,"目标玩家不能是自己！");
            return;
        }
        RdiIslandRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.put,
                fromPlayer,
                msg->{
                    if(msg.startsWith("-")){
                        sendChatMessage(fromPlayer, MessageType.ERROR,msg);
                        return;
                    }
                    TextUtils.sendChatMessage(fromPlayer,MessageType.SUCCESS,msg);
                    TextUtils.sendChatMessage(toPlayer,MessageType.SUCCESS, fromPlayer.getScoreboardName()+"把岛屿转让给了你！");
                },
                "island2/transfer/"+fromPlayer.getStringUUID()+"/"+toPlayer.getStringUUID()
        ));
    }

    private void createIsland(ServerPlayer player){
        sendChatMessage(player, MessageType.INFO,"准备创建岛屿，不要触碰鼠标或者键盘！");
        RdiIslandRequestThread.addTask(new RdiHttpPlayerRequest(
                        RdiHttpRequest.Type.post,
                        player,
                        msg->{
                            if(msg.startsWith("-")){
                                sendChatMessage(player, MessageType.ERROR,msg);
                                return;
                            }
                            sendChatMessage(player, MessageType.INFO,"开始创建岛屿，不要触碰鼠标或者键盘！");
                            int iid =Integer.parseInt(msg);
                            sendChatMessage(player, MessageType.INFO,"您的岛屿ID："+iid);
                            MinecraftServer server = RDICeleTech.getServer();
                            ServerUtils.executeOnServerThread(()->{
                                Fantasy fantasy = Fantasy.get(server);
                                sendChatMessage(player, MessageType.INFO,"正在创建存档。。");
                                ResourceLocation islandDimension = getIslandDimensionLoca(iid);
                                RuntimeWorldHandle worldHandle = fantasy.getOrOpenPersistentWorld(islandDimension, getIslandWorldConfig());
                                sendChatMessage(player, MessageType.INFO,"正在载入地形。。");
                                ServerLevel level = worldHandle.asWorld();
                                WorldUtils.placeInitialBlocks(level);
                                sendChatMessage(player, MessageType.INFO,"放置方块中。。");
                                PlayerUtils.addSlowFallEffect(player);
                                PlayerUtils.setSpawnPoint(player,level.dimension(),WorldUtils.INIT_POS.above(7));
                                sendChatMessage(player, MessageType.INFO,"准备传送。。。");
                                PlayerUtils.teleport(player,level,WorldUtils.INIT_POS.above(7));
                                sendChatMessage(player, MessageType.SUCCESS,"成功！");
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
        RdiIslandRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.put,
                player,
                resp-> {
                    if(resp.startsWith("-")){
                        sendChatMessage(player,MessageType.ERROR,resp);
                        return;
                    }
                    sendChatMessage(player,MessageType.SUCCESS,resp+"成功将您的空岛传送点改成了%f,%f,%f".formatted(x,y,z));
                },
                "island2/loca/"+player.getStringUUID(),
                "x="+x,"y="+y,"z="+z,"w="+w,"p="+p,"dim="+dim
        ));

    }
    private void kickPlayer(ServerPlayer fromPlayer, ServerPlayer kickPlayer) {
        if(fromPlayer==kickPlayer){
            TextUtils.sendChatMessage(fromPlayer, MessageType.ERROR,"您不可以踢出自己!!");
            return;
        }
        RdiIslandRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.delete,
                fromPlayer,
                response->{
                    if(response.equals("1")) {
                        TextUtils.sendChatMessage(kickPlayer, fromPlayer.getScoreboardName() + "删除了他的岛屿!");
                        PlayerUtils.resetProfile(kickPlayer);
                        sendChatMessage(fromPlayer, MessageType.SUCCESS, "1");
                    }else
                        sendChatMessage(fromPlayer,MessageType.ERROR,response);
                },
                "island2/crew/" +  kickPlayer.getStringUUID()
        ));
    }

    private void invitePlayer(ServerPlayer player, ServerPlayer invitedPlayer) {
        if(player==invitedPlayer){
            TextUtils.sendChatMessage(player,MessageType.ERROR,"目标玩家不能是自己！");
            return;
        }
        RdiIslandRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.post,
                player,
                response->{

                    TextUtils.sendChatMessage(player,"您邀请了"+invitedPlayer);
                    if(response.equals("1")){
                        TextUtils.sendChatMessage(invitedPlayer,MessageType.INFO,player.getScoreboardName()+"邀请您加入了他的岛屿");
                        TextUtils.sendClickableContent(invitedPlayer,"按下[H键]可以前往他的岛屿","/home");
                        sendChatMessage(player,MessageType.SUCCESS,"1");
                    }else
                        sendChatMessage(player,MessageType.ERROR,response);
                },
                "island2/crew/" + player.getStringUUID()+"/"+invitedPlayer.getStringUUID()
        ));
    }

    private void resetIsland(ServerPlayer player) {
        RdiIslandRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.delete,
                player,
                resp->{
                    if(resp.startsWith("-")){
                        sendChatMessage(player,MessageType.ERROR,resp);
                        return;
                    }
                    ResourceLocation dim = Island2Command.getIslandDimensionLoca(resp);
                    ServerUtils.executeOnServerThread(()-> {
                        RuntimeWorldHandle worldHandle = Fantasy.get(RDICeleTech.getServer()).getOrOpenPersistentWorld(dim, Island2Command.getIslandWorldConfig());
                        PlayerUtils.resetProfile(player);
                        worldHandle.delete();
                        sendChatMessage(player, MessageType.SUCCESS, resp);
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
