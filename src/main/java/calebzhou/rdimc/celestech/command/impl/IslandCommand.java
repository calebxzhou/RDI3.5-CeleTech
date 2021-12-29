package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.enums.ColorConst;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.model.PlayerHome;
import calebzhou.rdimc.celestech.utils.*;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

import static calebzhou.rdimc.celestech.constant.ServiceConstants.ADDR;

public class IslandCommand extends BaseCommand {
    public IslandCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }
    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource())).then(
                CommandManager.argument("opr", StringArgumentType.string())
                        .executes(context ->
                    execute(context.getSource(),StringArgumentType.getString(context,"opr"))
                )
        );
    }



    private int execute(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        TextUtils.sendChatMessage(player, ColorConst.GOLD+"----RDI CeleTech3----");
        MutableText createText = TextUtils.getClickableContentComp(ColorConst.BRIGHT_GREEN+"[创建空岛]","/island create","");
        MutableText deleteText = TextUtils.getClickableContentComp(ColorConst.RED+"[删除空岛]","/island delete","");
        MutableText joinText = TextUtils.getClickableContentComp(ColorConst.GOLD+"[加入朋友的空岛]","/island join","");
        MutableText locateText = TextUtils.getClickableContentComp(ColorConst.AQUA+"[改变空岛传送点]","/island locate","");
        TextUtils.sendChatMessage(player,TextUtils.concatTexts(createText,deleteText,joinText,locateText));
        /*ThreadPool.newThread(()->{
            String homeResp = HttpUtils.doGet(ServiceConstants.ADDR+
                    String.format("home?action=GET&playerUuid=%s&homeName=island", player.getUuidAsString()));
            //没有空岛
            if(homeResp == null||homeResp.equals("{}")){
                createIsland(player);
                return;
            }
            //有空岛

            PlayerHome home = new Gson().fromJson(homeResp, PlayerHome.class);
            PlayerUtils.teleportPlayer(player,home.getDimension(),home.getPosX(),home.getPosY(),home.getPosZ(),home.getYaw(),home.getPitch());
        });*/


        return 1;
    }
    private int execute(ServerCommandSource source, String opr) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        switch (opr){
            case "create" -> createIsland(player);
            case "delete" -> deleteIsland(player);
            case "locate" -> locateIsland(player);
            default -> {
                String islandId = opr.split(":")[1];
                if(islandId==null || islandId.length()==0){
                    TextUtils.sendChatMessage(player,"空岛ID无效！");
                    return 1;
                }
                if(opr.startsWith("joinIsland")){
                    joinIsland(player,islandId);
                }else if(opr.startsWith("quitIsland")){
                    quitIsland(player,islandId);
                }
            }
        }

        return 1;
    }
    private boolean hasIsland(ServerPlayerEntity player){
        return Boolean.parseBoolean(HttpUtils.doGet(ADDR + "island?action=HAS&uuid=" + player.getUuidAsString()));
    }
    private boolean hasJoinedIsland(ServerPlayerEntity player){
        return Boolean.parseBoolean(HttpUtils.doGet(ADDR + "island?action=JOINED&uuid=" + player.getUuidAsString()));
    }
    private String getIslandId(ServerPlayerEntity player){
        return HttpUtils.doGet(ADDR+"island?action=GETID&uuid="+player.getUuidAsString());
    }
    private void joinIsland(ServerPlayerEntity player,String island){

    }
    private void quitIsland(ServerPlayerEntity player,String island){
        ThreadPool.newThread(()->{
                    if(hasJoinedIsland(player)){
                        String iid = getIslandId(player);
                        boolean succ = Boolean.parseBoolean(HttpUtils.doGet(ADDR + "island?action=QUIT&islandId=" + iid+"&uuid="+player.getUuidAsString()));
                        if(succ){
                            PlayerUtils.resetSpawnPoint(player);
                            player.getInventory().clear();
                            player.kill();
                            TextUtils.sendChatMessage(player,"成功退出了空岛！");
                        }else{
                            TextUtils.sendChatMessage(player,"退出失败，"+succ);
                        }
                        return;
                    }else{
                        TextUtils.sendChatMessage(player,"您没有加入任何一个空岛，无法退出！");
                    }
                }
        );
    }
    private void locateIsland(ServerPlayerEntity player){

    }
    private void deleteIsland(ServerPlayerEntity player){
        ThreadPool.newThread(()->{
            if(hasIsland(player)){
                String iid = getIslandId(player);
                boolean succ = Boolean.parseBoolean(HttpUtils.doGet(ADDR + "island?action=DELETE&islandId=" + iid));
                if(succ){
                    TextUtils.sendChatMessage(player,"成功删除了空岛！");
                    player.getInventory().clear();
                    player.kill();
                }else{
                    TextUtils.sendChatMessage(player,"删除失败，"+succ);
                }
                return;
            }else{
                TextUtils.sendChatMessage(player,"您没有空岛，无法删除！");
            }
        }
        );
    }
    private void createIsland(ServerPlayerEntity player){
        ThreadPool.newThread(()->{
            if(hasIsland(player)){
                TextUtils.sendChatMessage(player,"您已经有一个空岛了，必须删除才能重新创建空岛！");
                return;
            }
            //随机传送，放黑曜石，给物品
            TextUtils.sendTitle(player, ColorConst.RED+"创建空岛 不要触碰键盘！");
            TextUtils.sendChatMessage(player,"创建空岛10%");
            //随机坐标
            CoordLocation location = RandomUtils.getRandomCoordinate();

            PlayerUtils.teleport(player,location.add(0.5,3,0.5));
            PlayerUtils.placeBlock(player.getWorld(),location,"minecraft:obsidian");

            TextUtils.sendChatMessage(player,"创建空岛20%");
            TextUtils.sendTitle(player, ColorConst.RED+"创建空岛 不要触碰键盘！");

                TextUtils.sendTitle(player, ColorConst.RED+"创建空岛 不要触碰键盘！");
                Island island = new Island(
                        RandomUtils.getRandomId(8),
                        player.getUuidAsString(),
                        location
                );
                HttpUtils.postObject("island",island,"action=CREATE");
                TextUtils.sendChatMessage(player,"创建空岛30%");
                //设置家
                TextUtils.sendChatMessage(player,"创建空岛50%");
                TextUtils.sendTitle(player, ColorConst.RED+"创建空岛 不要触碰键盘！");
                PlayerUtils.givePlayerInitialKit(player);
                TextUtils.sendChatMessage(player,"创建空岛80%");
                TextUtils.sendTitle(player, ColorConst.RED+"创建空岛 不要触碰键盘！");
                TextUtils.sendChatMessage(player,"创建空岛100%");
                TextUtils.sendTitle(player, ColorConst.RED+"创建空岛 不要触碰键盘！");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TextUtils.sendTitle(player, ColorConst.BRIGHT_GREEN+"成功创建空岛！");


        });
    }
}
