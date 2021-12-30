package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.enums.ColorConst;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.model.PlayerHome;
import calebzhou.rdimc.celestech.utils.*;
import com.google.gson.Gson;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

import java.util.ArrayList;

import static calebzhou.rdimc.celestech.constant.ServiceConstants.ADDR;

public class IslandCommand extends BaseCommand {
    public IslandCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }
    public static final ArrayList<String> subcommands=new ArrayList<>();
    static{
        subcommands.add("create");
        subcommands.add("delete");
        subcommands.add("join");
        subcommands.add("locate");
        subcommands.add("invite");
        subcommands.add("home");
        subcommands.add("quit");
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
        TextUtils.sendChatMessage(player, ColorConst.GOLD+"----RDI CeleTech3----");
        ThreadPool.newThread(()->{
            MutableText createText = TextUtils.getClickableContentComp(ColorConst.BRIGHT_GREEN+"[创建空岛]","/island create","");
            MutableText deleteText = TextUtils.getClickableContentComp(ColorConst.RED+"[删除空岛]","/island delete","");
            MutableText joinText = TextUtils.getClickableContentComp(ColorConst.GOLD+"[加入朋友的空岛]","/island join","");
            MutableText locateText = TextUtils.getClickableContentComp(ColorConst.AQUA+"[改变空岛传送点]","/island locate","");
            MutableText inviteText = TextUtils.getClickableContentComp(ColorConst.AQUA+"[邀请朋友加入我的空岛]","/island invite","");
            MutableText homeT = TextUtils.getClickableContentComp(ColorConst.AQUA+"[返回空岛]","/island home","");
            //没有空岛的时候，只显示创建和加入
            if(!hasIsland(player))
                TextUtils.sendChatMessage(player,TextUtils.concatTexts(createText,joinText));
            else//有空岛的时候
                TextUtils.sendChatMessage(player, TextUtils.concatTexts(deleteText,locateText,inviteText,homeT));

        });

        return 1;
    }
    private int execute(ServerCommandSource source, String opr) throws CommandSyntaxException {
        ServerUtils.save();
        ServerPlayerEntity player = source.getPlayer();
        switch (opr){
            case "create" -> createIsland(player);
            case "delete" -> deleteIsland(player);
            case "locate" -> locateIsland(player);
            case "home" -> goBackIsland(player);
            case "invite"-> invite(player);
            case "quit" ->quitIsland(player);
            default -> {
                String islandId = opr.split("-")[1];
                if(islandId==null || islandId.length()==0){
                    TextUtils.sendChatMessage(player,"空岛ID无效！");
                    return 1;
                }
                if(opr.startsWith("join")){
                    joinIsland(player,islandId);
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
    private String getJoinedIslandId(ServerPlayerEntity player){
        return HttpUtils.doGet(ADDR+"island?action=GETID_JOINED&uuid="+player.getUuidAsString());
    }
    private void invite(ServerPlayerEntity player) {
        ThreadPool.newThread(()->{
            if(!hasIsland(player)){
                TextUtils.sendChatMessage(player,"您没有空岛！！");
                return;
            }
            String islandId=getIslandId(player);
            TextUtils.sendChatMessage(player,"您的空岛ID："+islandId+",将这个空岛ID分享给朋友，令其输入/island join-"+islandId+" 这个指令，便可。任何人都能通过这个ID加入到你的空岛，因此不要随便泄露空岛ID，否则容易导致恶意破坏！！！");

        });
    }
    private void goBackIsland(ServerPlayerEntity player){
        ThreadPool.newThread(()->{
            Island island = null;
            if(hasIsland(player)){

                island = new Gson().fromJson(
                        HttpUtils.doGet(ADDR+"island?action=GET&islandId="+getIslandId(player)
                        ),Island.class);

            }else if(hasJoinedIsland(player)){
                island = new Gson().fromJson(HttpUtils.doGet(ADDR+"island?action=GET_JOINED&islandId="+getJoinedIslandId(player)),Island.class);
            }
            if(island==null){
                TextUtils.sendChatMessage(player,"没有空岛！");
                return;
            }
            PlayerUtils.teleport(player,CoordLocation.fromString(island.getLocation()).add(0.5,1,0.5));
            TextUtils.sendChatMessage(player,"已经回到了您的空岛！");
        });

    }
    private void joinIsland(ServerPlayerEntity player,String island){
        ThreadPool.newThread(()->{
            HttpUtils.doGet(ADDR+"island?action=JOIN&uuid="+player.getUuidAsString()+"&islandId="+island);
            TextUtils.sendChatMessage(player,"成功加入空岛！");
        });
    }
    private void quitIsland(ServerPlayerEntity player){
        ThreadPool.newThread(()->{
                    if(hasJoinedIsland(player)){
                        boolean succ = Boolean.parseBoolean(HttpUtils.doGet(ADDR + "island?action=QUIT&islandId=" + getJoinedIslandId(player)+"&uuid="+player.getUuidAsString()));
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
        ThreadPool.newThread(()->{
                    if(!hasIsland(player)){
                        TextUtils.sendChatMessage(player,"您未创建任何一个空岛， 无法重新设置传送点。");
                    }
                    String coor=CoordLocation.fromPlayer(player).toString();
                    boolean set=Boolean.parseBoolean(HttpUtils.doGet(ADDR+"island?action=LOCATE&islandId="+getIslandId(player)+"&location="+coor));
                    TextUtils.sendChatMessage(player,(set?"成功":"失败")+"设定您的空岛传送点"+coor);
                }
        );
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
            Island island = new Island(
                    RandomUtils.getRandomId(8),
                    player.getUuidAsString(),
                    location
            );
            HttpUtils.postObject("island",island,"&action=CREATE");
            PlayerUtils.teleport(player,location.add(0.5,3,0.5));
            PlayerUtils.placeBlock(player.getWorld(),location,"minecraft:obsidian");

            TextUtils.sendChatMessage(player,"创建空岛20%");
            TextUtils.sendTitle(player, ColorConst.RED+"创建空岛 不要触碰键盘！");
                TextUtils.sendChatMessage(player,"创建空岛30%");
                //设置家
                TextUtils.sendChatMessage(player,"创建空岛50%");
                TextUtils.sendTitle(player, ColorConst.RED+"创建空岛 不要触碰键盘！");
                TextUtils.sendChatMessage(player,"创建空岛80%");
                TextUtils.sendTitle(player, ColorConst.RED+"创建空岛 不要触碰键盘！");
                TextUtils.sendChatMessage(player,"创建空岛100%");
                TextUtils.sendTitle(player, ColorConst.RED+"创建空岛 不要触碰键盘！");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            PlayerUtils.givePlayerInitialKit(player);
                TextUtils.sendTitle(player, ColorConst.BRIGHT_GREEN+"成功创建空岛！");


        });
    }
}
