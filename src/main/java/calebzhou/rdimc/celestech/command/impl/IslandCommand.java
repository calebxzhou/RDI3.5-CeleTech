package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.ServiceConstants;
import calebzhou.rdimc.celestech.enums.EColor;
import calebzhou.rdimc.celestech.model.PlayerHome;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.google.gson.Gson;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class IslandCommand extends BaseCommand {
    public IslandCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }
    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();

        ThreadPool.newThread(()->{
            String homeResp = HttpUtils.doGet(ServiceConstants.ADDR+
                    String.format("home?action=GET&playerUuid=%s&homeName=island", player.getUuidAsString()));
            //没有空岛
            if(homeResp == null||homeResp.equals("{}")){
                createIsland(player);
                return;
            }
            //有空岛
            TextUtils.sendChatMessage(player,"准备回到空岛");
            PlayerHome home = new Gson().fromJson(homeResp, PlayerHome.class);
            PlayerUtils.teleportPlayer(player,home.getDimension(),home.getPosX(),home.getPosY(),home.getPosZ(),home.getYaw(),home.getPitch());
        });


        return 1;
    }

    private void createIsland(ServerPlayerEntity player){
        //随机传送，放黑曜石，给物品
        TextUtils.sendChatMessage(player,"创建空岛10%");
        PlayerUtils.randomTeleport(player);
        TextUtils.sendChatMessage(player,"创建空岛20%");
        ThreadPool.newThread(()->{
            PlayerHome playerHome = PlayerHome.fromPlayerLocation(player,"island");
            TextUtils.sendChatMessage(player,"创建空岛30%");
            //设置家
            HttpUtils.postObject("home",playerHome,"&action=SET");
            TextUtils.sendChatMessage(player,"创建空岛50%");
                player.getInventory().insertStack(new ItemStack(Items.ACACIA_SAPLING,1));
                player.getInventory().insertStack(new ItemStack(Items.WATER_BUCKET,1));
                player.getInventory().insertStack(new ItemStack(Items.LAVA_BUCKET,1));
                player.getInventory().insertStack(new ItemStack(Items.GRASS_BLOCK,8));
                player.getInventory().insertStack(new ItemStack(Items.DIRT,16));
                player.getInventory().insertStack(new ItemStack(Items.CHEST,1));
            TextUtils.sendChatMessage(player,"创建空岛80%");
            TextUtils.sendChatMessage(player,"创建空岛100%");

        });
    }
}
