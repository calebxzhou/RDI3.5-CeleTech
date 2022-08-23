package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.constant.WorldConst;
import calebzhou.rdimc.celestech.thread.RdiHttpPlayerRequest;
import calebzhou.rdimc.celestech.thread.RdiHttpRequest;
import calebzhou.rdimc.celestech.thread.RdiIslandRequestThread;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.WorldUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
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
            =====RDI空岛管理菜单=====
            指令参数
            create 创建岛屿
            reset 重置岛屿
            kick 移除成员
            invite 邀请成员
            loca 更改传送点
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
        return new ResourceLocation(RDICeleTech.MODID, RDICeleTech.ISLAND_DIMENSION_PREFIX + iid);
    }
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> sendIslandHelp(context.getSource().getPlayer()))
                .then(
                        Commands.argument("指令参数", StringArgumentType.string())
                                .suggests(
                                        (context, builder) ->
                                                SharedSuggestionProvider.suggest(new String[]{"create","reset","kick","invite","loca"},builder)
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
            default -> sendIslandHelp(fromPlayer);
        }
        return 1;
    }



    private int handleSubCommand(ServerPlayer player, String param) {
        switch (param){
            case "create" -> createIsland(player);
            case "reset" -> resetIsland(player);
            case "loca" -> locateIsland(player);
            default -> sendIslandHelp(player);
        }
        return 1;
    }
    private void createIsland(ServerPlayer player){
        sendChatMessage(player, MessageType.INFO,"准备创建岛屿，不要触碰鼠标或者键盘！");
        RdiIslandRequestThread.addTask(new RdiHttpPlayerRequest(
                        RdiHttpRequest.Type.post,
                        player,
                        msg->{

                            if ("0".equals(msg)) {
                                sendChatMessage(player, MessageType.ERROR,"！");
                                return;
                            }
                            sendChatMessage(player, MessageType.INFO,"开始创建岛屿，不要触碰鼠标或者键盘！");
                            int iid =Integer.parseInt(msg);
                            sendChatMessage(player, MessageType.INFO,"您的岛屿ID："+iid);
                            Fantasy fantasy = Fantasy.get(RDICeleTech.getServer());

                            sendChatMessage(player, MessageType.INFO,"正在创建存档。。");
                            ResourceLocation islandDimension = getIslandDimensionLoca(iid);
                            RuntimeWorldHandle worldHandle = fantasy.getOrOpenPersistentWorld(islandDimension, getIslandWorldConfig());
                            sendChatMessage(player, MessageType.INFO,"正在载入地形。。");
                            ServerLevel level = worldHandle.asWorld();
                            WorldUtils.placeInitialBlocks(level);
                            sendChatMessage(player, MessageType.INFO,"放置方块中。。");
                            PlayerUtils.addSlowFallEffect(player);
                            PlayerUtils.setSpawnPoint(player,level.dimension(),WorldUtils.INIT_POS.above(2));
                            sendChatMessage(player, MessageType.INFO,"准备传送。。。");
                            PlayerUtils.teleport(player,level,WorldUtils.INIT_POS.above(15));
                            sendChatMessage(player, MessageType.SUCCESS,"成功！");
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
        RdiIslandRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.put,
                player,
                resp-> sendChatMessage(player,resp),
                "island2/"+player.getStringUUID(),
                "x="+x,"y="+y,"z="+z,"w="+w,"p="+p
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
                        sendChatMessage(fromPlayer, MessageType.SUCCESS, "1");
                    }else
                        sendChatMessage(fromPlayer,MessageType.ERROR,response);
                },
                "island2/crew/" + fromPlayer.getStringUUID()+"/"+kickPlayer.getStringUUID()
        ));
    }

    private void invitePlayer(ServerPlayer player, ServerPlayer invitedPlayer) {
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
                RdiHttpRequest.Type.get,
                player,
                resp->{
                    if(!resp.equals("0")){
                        String[] split = resp.split(",");
                        String iid = split[0];
                        RdiIslandRequestThread.addTask(new RdiHttpPlayerRequest(RdiHttpRequest.Type.delete,player,resp2->{
                            if (resp2.equals("1")) {
                                ResourceLocation dim = Island2Command.getIslandDimensionLoca(iid);
                                RuntimeWorldHandle worldHandle = Fantasy.get(RDICeleTech.getServer()).getOrOpenPersistentWorld(dim, Island2Command.getIslandWorldConfig());
                                player.getInventory().clearContent();
                                player.kill();
                                PlayerUtils.teleport(player, WorldConst.SPAWN_LOCA);
                                worldHandle.delete();
                                PlayerUtils.setSpawnPoint(player,Level.OVERWORLD, new BlockPos(WorldConst.SPAWN_LOCA.x, WorldConst.SPAWN_LOCA.y, WorldConst.SPAWN_LOCA.z));
                                sendChatMessage(player, MessageType.SUCCESS, "1");
                            } else {
                                sendChatMessage(player, MessageType.ERROR, "您未拥有空岛！");
                            }
                        },"island2/"+player.getStringUUID()));
                    }else{
                        sendChatMessage(player,MessageType.ERROR,resp);
                    }
                    /*if (resp.equals("1")) {
                        player.getInventory().clearContent();
                        player.kill();
                        PlayerUtils.teleport(player, WorldConst.SPAWN_LOCA);
                        player.setRespawnPosition(Level.OVERWORLD, new BlockPos(WorldConst.SPAWN_LOCA.x, WorldConst.SPAWN_LOCA.y, WorldConst.SPAWN_LOCA.z), 0, true, false);
                        sendChatMessage(player, MessageType.SUCCESS, "1");
                    } else {
                        sendChatMessage(player, MessageType.ERROR, "您未拥有空岛！");
                    }*/
                },
                "island2/" + player.getStringUUID()
        ));
    }

    private int sendIslandHelp(ServerPlayer player) {
        TextUtils.sendChatMultilineMessage(player,islandHelp);
        return 1;
    }

}
