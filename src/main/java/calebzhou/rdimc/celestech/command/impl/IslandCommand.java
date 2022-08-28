package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.constant.WorldConst;
import calebzhou.rdimc.celestech.model.PlayerLocation;
import calebzhou.rdimc.celestech.thread.RdiHttpPlayerRequest;
import calebzhou.rdimc.celestech.thread.RdiHttpRequest;
import calebzhou.rdimc.celestech.thread.RdiIslandRequestThread;
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
            create 创建岛屿
            reset 重置岛屿
            kick 移除成员
            invite 邀请成员
            loca 更改传送点
            ====================
            """;
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
            //case "create" -> createIsland(player);
            case "reset" -> resetIsland(player);
            case "loca" -> locateIsland(player);
            default -> sendIslandHelp(player);
        }
        return 1;
    }

    private void locateIsland(ServerPlayer player) {
        RdiIslandRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.put,
                player,
                resp-> sendChatMessage(player,resp),
                "island/"+player.getStringUUID()+"/"+new PlayerLocation(player).toInt().getXyzComma()
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
                    }
                },
                "island/crew/" + fromPlayer.getStringUUID()+"/"+kickPlayer.getStringUUID()
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
                    }
                },
                "island/crew/" + player.getStringUUID()+"/"+invitedPlayer.getStringUUID()
        ));
    }

    private void resetIsland(ServerPlayer player) {
        RdiIslandRequestThread.addTask(new RdiHttpPlayerRequest(
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
    private void createIsland(ServerPlayer player){

        int x = RandomUtils.generateRandomInt(-99999, 99999);
        int y = 128;
        int z = RandomUtils.generateRandomInt(-99999, 99999);

        RdiIslandRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.post,
                player,
                msg->{
                    if ("0".equals(msg)) {
                        sendChatMessage(player, MessageType.ERROR,"您已经加入了一个岛屿！");
                        return;
                    }
                    /*
                    3.7
                    Fantasy fantasy = Fantasy.get(RDICeleTech.getServer());
                    RuntimeWorldConfig worldConfig = new RuntimeWorldConfig()
                            .setDimensionType(BuiltinDimensionTypes.OVERWORLD)
                            .setDifficulty(Difficulty.HARD)
                            .setGenerator(RDICeleTech.getServer().overworld().getChunkSource().getGenerator())
                            .setSeed(System.currentTimeMillis());

                    ResourceLocation islandDimension = new ResourceLocation(RDICeleTech.MODID, ISLAND_DIMENSION_PREFIX + islandId);
                    RuntimeWorldHandle worldHandle = fantasy.getOrOpenPersistentWorld(islandDimension, worldConfig);*/
                    PlayerLocation loca = new PlayerLocation(x,y,z);
                    ResourceKey<Level> resourceKey = player.getLevel().dimension();
                    loca.world= player.getLevel();
                    player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,20*30,1));
                    player.setRespawnPosition(resourceKey,new BlockPos(loca.x,loca.y,loca.z).above(2),0,true,false);
                    teleport(player, loca.add(0.5, 12,  0.5));
                    sendChatMessage(player, MessageType.SUCCESS,"成功！");
                    placeBlock(player.getLevel(), loca, Blocks.BEDROCK.defaultBlockState());
                    placeBlock(player.getLevel(), loca.add(1,0,0), Blocks.DIRT.defaultBlockState());
                    placeBlock(player.getLevel(), loca.add(1,1,0), Blocks.OAK_SAPLING.defaultBlockState());
                    //givePlayerInitialKit(player);
                },
                "island/"+ player.getStringUUID()
                ,"x="+x,"y="+y,"z="+z
                )
        );
/* OptionalLong fixedTime=null;
        int coordinateScale=1;
        boolean hasSkyLight=true, hasCeiling=false, ultraWarm=false, natural=true , bedWorks=true, respawnAnchorWorks=true;
        int minY=-64 , height=384, logicalHeight=384;
        TagKey<Block> infiniburn= BlockTags.INFINIBURN_OVERWORLD;
        float ambientLight=0f;
        DimensionType.MonsterSettings monsterSetting = new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 0);
        ResourceLocation overworldEffects = BuiltinDimensionTypes.OVERWORLD_EFFECTS;

        DimensionType dimensionType = new DimensionType(OptionalLong.empty(),hasSkyLight,hasCeiling,ultraWarm,natural,coordinateScale,bedWorks,respawnAnchorWorks,minY,height,logicalHeight,infiniburn,overworldEffects,ambientLight,monsterSetting);
        ResourceKey<Level> newDimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(RDICeleTech.MODID, ISLAND_DIMENSION_PREFIX+RandomUtils.getRandomIslandId()));
        */
    }
}
