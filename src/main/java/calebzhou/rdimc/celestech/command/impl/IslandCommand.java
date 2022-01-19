package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.constant.WorldConstants;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.model.cache.IslandCache;
import calebzhou.rdimc.celestech.model.cache.UuidNameCache;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.utils.*;
import com.google.gson.Gson;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static calebzhou.rdimc.celestech.utils.TextUtils.*;

public class IslandCommand extends BaseCommand {
    public IslandCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }
    public static final ArrayList<String> subcommands=new ArrayList<>();
    static{
        subcommands.add("create");
        subcommands.add("delete");
        subcommands.add("join");
        subcommands.add("quit");
        subcommands.add("invite");
        subcommands.add("kick");
        subcommands.add("home");
        subcommands.add("sethome");
       // subcommands.add("melt");
        subcommands.add("biome-");
    }
    public static final SuggestionProvider<ServerCommandSource> SUGGESTION_PROVIDER = (context, builder) ->
            CommandSource.suggestMatching(subcommands, builder);

    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource())).then(
                CommandManager.argument("opr", StringArgumentType.string())
                        .executes(context ->
                    execute(context.getSource(),StringArgumentType.getString(context,"opr"))
                ).suggests(SUGGESTION_PROVIDER)
        );
    }



    private int execute(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        sendChatMessage(player, ColorConstants.GOLD+"----RDI CT3 by Davickk----(2022.01.07)");
        ThreadPool.newThread(()->{
            String islandOwnId = IslandCache.instance.getOwnIslandMap().get(player.getUuidAsString());
            final StringBuilder builder=new StringBuilder();
            if(islandOwnId != null){
                Island island = IslandCache.instance.getIslandMap().get(islandOwnId);
                if(island==null){
                    return;
                }
                builder.append("岛编号: ");
                builder.append(islandOwnId);
                builder.append(" 岛成员: ");
                Collection<String> memberIds = IslandCache.instance.getMemberMap().get(islandOwnId);
                if(!memberIds.isEmpty()){
                    Map<String, String> memIdNames = UuidNameCache.instance.getMap().entrySet().stream()
                            .filter(e -> memberIds.contains(e.getKey()))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    memIdNames.forEach((id,name)->{
                        if(ServerUtils.getOnlinePlayerList().contains(name))
                            builder.append(ColorConstants.BRIGHT_GREEN);
                        builder.append(name);
                        builder.append(ColorConstants.RESET);
                        builder.append(" ");
                    });
                }else{
                    builder.append("无");
                }
                sendChatMessage(player , builder.toString());
                StringBuilder b2 =new StringBuilder();
                b2.append("创建时间:");
                b2.append(island.getCreateTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                sendChatMessage(player,b2.toString());
            }else{
            } sendChatMessage(player,"若您没有空岛, 输入/island create");
            sendChatMessage(player,getClickableContentComp(ColorConstants.GOLD+"[返回空岛]","/island home","  "));
        });

        return 1;
    }
    private int execute(ServerCommandSource source, String opr) throws CommandSyntaxException {
        ServerUtils.save();
        ServerPlayerEntity player = source.getPlayer();
        sendActionMessage(player,"正在请求服务器....");
        switch (opr){
           // case "melt" ->meltObsidian(player);

            case "create" -> create(player);
            case "delete" -> delete(player);
            case "quit" -> quit(player);
            case "invite"-> invite(player);
            case "home" -> home(player);
            case "sethome" -> sethome(player);
            default -> {
                if(opr.startsWith("join"))
                    joinIsland(player,opr);
                else if(opr.startsWith("biome"))
                    makeBiome(player,opr);
                else sendChatMessage(player,"参数无效!");

            }
        }
        return 1;
    }
    private void create(ServerPlayerEntity player){
        ThreadPool.newThread(()->{
            String locaS = HttpUtils.sendRequest("POST","island/create/"+player.getUuidAsString());
            Island island = new Gson().fromJson(locaS, Island.class);
            CoordLocation iloca=CoordLocation.fromString(island.getLocation());
            PlayerUtils.teleport(player, iloca.add(0.5,3,0.5));
            PlayerUtils.placeBlock(player.getWorld(),iloca,"minecraft:obsidian");
            PlayerUtils.givePlayerInitialKit(player);
            sendTitle(player, ColorConstants.BRIGHT_GREEN+"成功创建空岛！");
        });
    }
    private void delete(ServerPlayerEntity player){
        ThreadPool.newThread(()->{
            HttpUtils.sendRequest("POST","island/delete/"+player.getUuidAsString());

        });
    }
    private void joinIsland(ServerPlayerEntity player,String island){
        String islandId="";
        try {
            islandId = island.split("-")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            sendChatMessage(player,"参数错误!您必须输入正确格式的空岛ID!"+e.getMessage());
        }
        boolLogic(player,"join","iid="+islandId);
    }
    private void quit(ServerPlayerEntity player){
        boolLogic(player,"quit",null);
    }

    private void invite(ServerPlayerEntity player) {
        ThreadPool.newThread(()->{
            String post = HttpUtils.post("island", "action=invite", "pid=" + player.getUuidAsString());
            sendChatMessage(player,post);
        });
    }
    private void home(ServerPlayerEntity player){
        if(PlayerUtils.getDimensionName(player).equals("minecraft:the_nether") && !player.getEntityName().equals("sampsonnzx")){
            sendChatMessage(player,"必须在主世界使用本指令!",MessageType.ERROR);
            return;
        }
            String iid = IslandCache.instance.getOwnIslandMap().get(player.getUuidAsString());
            if(iid == null){
                ThreadPool.newThread(()->{
                    String locaS = HttpUtils.post("island", "action=home", "pid=" + player.getUuidAsString());
                    sendActionMessage(player,locaS);
                    CoordLocation location = CoordLocation.fromString(locaS);
                    if(location==null){
                        sendChatMessage(player,"获取空岛位置失败:"+locaS);
                        return;
                    }
                    PlayerUtils.teleport(player,location.add(0.5,5,0.5));
                    sendActionMessage(player,"已经回到了您的空岛！");
                });
                return;
            }
            Island island = IslandCache.instance.getIslandMap().get(iid);
            if(island == null){
                ThreadPool.newThread(()->{
                    String locaS = HttpUtils.post("island", "action=home", "pid=" + player.getUuidAsString());
                    sendActionMessage(player,locaS);
                    CoordLocation location = CoordLocation.fromString(locaS);
                    if(location==null){
                        sendChatMessage(player,"获取空岛位置失败:"+locaS);
                        return;
                    }
                    PlayerUtils.teleport(player,location.add(0.5,5,0.5));
                    sendActionMessage(player,"已经回到了您的空岛！");
                });
                return;
            }
            /*String locaS = HttpUtils.post("island", "action=home", "pid=" + player.getUuidAsString());
            sendActionMessage(player,locaS);*/
            CoordLocation location = CoordLocation.fromString(island.getLocation());
            if(location==null){
                sendChatMessage(player,"获取空岛位置失败:"+location);
                return;
            }
            PlayerUtils.teleport(player,location.add(0.5,5,0.5));
            sendChatMessage(player,"已经回到了您的空岛！",MessageType.SUCCESS);

    }

    private void sethome(ServerPlayerEntity player){
        boolLogic(player,"sethome","loca="+CoordLocation.fromPlayer(player).toString());
        sendChatMessage(player,"(成功)请注意，传送点必须设置在至少3x3平地的中间！", MessageType.INFO);
    }
    private void boolLogic(ServerPlayerEntity player,String action,@Nullable String param){
        ThreadPool.newThread(()->{
             String result = HttpUtils.post("island","action="+action,"pid="+player.getUuidAsString(),param);
                    if(result.contains("true")){
                        sendActionMessage(player,"成功");
                        if(action.equals("delete")){
                            PlayerUtils.teleport(player,WorldConstants.SPAWN_LOCA);
                            player.getInventory().clear();
                            player.kill();
                        }
                    }else
                        sendActionMessage(player,"错误:"+ result);

        }
        );

    }
    private void makeBiome(ServerPlayerEntity player, String biomeType) {
        HttpUtils.asyncSendObject(new GenericRecord(player.getUuidAsString(), RecordType.biome,CoordLocation.fromPlayer(player).toString(),biomeType,null));
        sendActionMessage(player,"已经提交更改请求,请等待处理.");
        /*Chunk chunk = player.getWorld().getChunk(player.getBlockX()>>4,player.getBlockZ()>>4);
        PalettedContainer<Biome> biomeArray = chunk.getSection(chunk.getSectionIndex(player.getBlockY())).getBiomeContainer();
        biomeArray.swap(
                player.getBlockX()&3,
                player.getBlockY()&3,
                player.getBlockZ()&3,
                new Biome.Builder().category(Biome.Category.byName(biomeType)).build());
        chunk.setShouldSave(true);*/
    }



    private void meltObsidian(ServerPlayerEntity player) {
        BlockPos obsidianBlock = PlayerUtils.getPlayerLookingAtBlock(player,false);
        if(obsidianBlock==null){
            sendActionMessage(player,"您不能熔化空气!!!!");
            return;
        }
        BlockState obsState = player.getWorld().getBlockState(obsidianBlock);
        if(player.experienceLevel>1){
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BAD_OMEN,15*20,5));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,15*20,1));
            sendActionMessage(player,"我抄,麻了");
            return;
        }
        if(obsState.getBlock() == Blocks.OBSIDIAN){
            player.getWorld().setBlockState(obsidianBlock, Fluids.LAVA.getDefaultState().getBlockState());
            player.damage(DamageSource.HOT_FLOOR,9.9f);
            sendActionMessage(player,"成功把黑曜石熔化成了岩浆!!");
        }else{
            sendActionMessage(player,"您必须对准黑曜石!!");
        }
    }




}
