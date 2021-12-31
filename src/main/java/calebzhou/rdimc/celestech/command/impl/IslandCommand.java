package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.Island;
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
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.PalettedContainer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static calebzhou.rdimc.celestech.constant.ServiceConstants.ADDR;
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
        subcommands.add("tps");
        subcommands.add("melt");
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
        sendChatMessage(player, ColorConstants.GOLD+"----RDI CeleTech3 by Davickk----");
        sendChatMessage(player,"欢迎使用RDI空岛系统,请到群文件查看指令列表.");
        return 1;
    }
    private int execute(ServerCommandSource source, String opr) throws CommandSyntaxException {
        ServerUtils.save();
        ServerPlayerEntity player = source.getPlayer();
        PlayerUtils.sendLoading(player);
        switch (opr){
            case "tps" ->tps(player);
            case "melt" ->meltObsidian(player);

            case "create" -> create(player);
            case "delete" -> delete(player);
            case "quit" -> quit(player);
            case "invite"-> invite(player);
            case "home" -> home(player);
            case "sethome" -> sethome(player);
            default -> {
                if(opr.length()==0){sendChatMessage(player,"指令无效");return 0;}
                String cmd = opr.split("-")[0];
                String arg2 = opr.split("-")[1];
                if(arg2==null || cmd==null){sendChatMessage(player,"参数无效!");return 0;}
                if(arg2.length() == 0 || cmd.length()==0){sendChatMessage(player,"参数无效!");return 0;}
                switch (cmd){
                    case "join" -> joinIsland(player,arg2);
                    case "biome" -> makeBiome(player,arg2);
                    default -> sendChatMessage(player,"参数无效!");
                }
            }
        }
        return 1;
    }
    private void create(ServerPlayerEntity player){
        PlayerUtils.sendLoading(player);
        ThreadPool.newThread(()->{
            String locaS = HttpUtils.post("island", "action=create", "pid=" + player.getUuidAsString());
            sendChatMessage(player,"请您记住空岛的位置坐标. "+locaS);
            CoordLocation location = CoordLocation.fromString(locaS);
            if(location==null){
                sendChatMessage(player,locaS+".错误!");
                return;
            }
            PlayerUtils.teleport(player,location.add(0.5,3,0.5));
            PlayerUtils.placeBlock(player.getWorld(),location,"minecraft:obsidian");
            sendTitle(player, ColorConstants.RED+"创建空岛 不要触碰键盘！");
            PlayerUtils.givePlayerInitialKit(player);
            sendTitle(player, ColorConstants.BRIGHT_GREEN+"成功创建空岛！");
        });
    }
    private void delete(ServerPlayerEntity player){
        boolLogic(player,"delete",null);
    }
    private void joinIsland(ServerPlayerEntity player,String island){
        boolLogic(player,"join","iid="+island);
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
        ThreadPool.newThread(()->{
            String locaS = HttpUtils.post("island", "action=home", "pid=" + player.getUuidAsString());
            sendChatMessage(player,"请您记住空岛的位置坐标. "+locaS);
            CoordLocation location = CoordLocation.fromString(locaS);
            if(location==null){
                sendChatMessage(player,locaS+".错误!");
                return;
            }
            PlayerUtils.teleport(player,location.add(0.5,1,0.5));
            sendChatMessage(player,"已经回到了您的空岛！");
        });

    }

    private void sethome(ServerPlayerEntity player){
        boolLogic(player,"sethome","loca="+CoordLocation.fromPlayer(player).toString());
    }
    private void boolLogic(ServerPlayerEntity player,String action,@Nullable String param){
        ThreadPool.newThread(()->{
            String result = HttpUtils.post("island","action="+action,"pid="+player.getUuidAsString(),param);
                    if(result.equals("true")){
                        sendChatMessage(player,"成功");
                    }else sendChatMessage(player,"失败 "+result);
                }
        );
    }
    private void makeBiome(ServerPlayerEntity player, String biomeType) {
        Chunk chunk = player.getWorld().getChunk(player.getBlockX()>>4,player.getBlockZ()>>4);
        PalettedContainer<Biome> biomeArray = chunk.getSection(chunk.getSectionIndex(player.getBlockY())).getBiomeContainer();
        biomeArray.swap(
                player.getBlockX()&3,
                player.getBlockY()&3,
                player.getBlockZ()&3,
                new Biome.Builder().category(Biome.Category.byName(biomeType)).build());
        chunk.setShouldSave(true);
    }

    private void tps(ServerPlayerEntity player) {
        double meanTickTime = MathUtils.getAverageValue(RDICeleTech.getServer().lastTickLengths) * 1.0E-6D;
        double stdTickTime = 120.0;
        double meanTPS = Math.min(1000.0 / meanTickTime, 20);
        double ratio = meanTickTime / stdTickTime;
        long squares = Math.round(25 * ratio);
        String squarePattern1 = ">";
        String squaresToSend = "§a";

        for (int i = 0; i <= squares; ++i) {
            squaresToSend += squarePattern1;
            if (i == 15)
                squaresToSend += "§e";
            if (i == 22)
                squaresToSend += "§c";
        }
        sendChatMessage(player,"负载[" + Math.round(ratio * 100) + "%/"+meanTPS*5+"tps]" + squaresToSend);
        sendChatMessage(player,"延迟 " + Math.round(meanTickTime) + "ms");

    }

    private void meltObsidian(ServerPlayerEntity player) {
        BlockPos obsidianBlock = PlayerUtils.getPlayerLookingAtBlock(player,false);
        if(obsidianBlock==null){
            sendChatMessage(player,"您不能熔化空气!!!!");
            return;
        }
        BlockState obsState = player.getWorld().getBlockState(obsidianBlock);
        if(player.experienceLevel>1){
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BAD_OMEN,15*20,5));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,15*20,1));
            sendChatMessage(player,"我抄,麻了");
            return;
        }
        if(obsState.getBlock() == Blocks.OBSIDIAN){
            player.getWorld().setBlockState(obsidianBlock, Fluids.LAVA.getDefaultState().getBlockState());
            player.damage(DamageSource.HOT_FLOOR,9.9f);
            sendChatMessage(player,"成功把黑曜石熔化成了岩浆!!");
        }else{
            sendChatMessage(player,"您必须对准黑曜石!!");
        }
    }




}
