package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.command.NoArgCommand;
import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.google.gson.Gson;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendTitle;

public class CreateIslandCommand extends NoArgCommand {
    public CreateIslandCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }

    @Override
    protected int execute(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        ThreadPool.newThread(()->{
            ApiResponse response = HttpUtils.sendRequest("POST","island/create/"+player.getUuidAsString());
            if(response.getType().equals("success")){
                Island island = new Gson().fromJson(response.getData(), Island.class);
                CoordLocation iloca=CoordLocation.fromString(island.getLocation());
                PlayerUtils.teleport(player, iloca.add(0.5,3,0.5));
                PlayerUtils.placeBlock(player.getWorld(),iloca,"minecraft:obsidian");
                PlayerUtils.givePlayerInitialKit(player);
                sendTitle(player, ColorConstants.BRIGHT_GREEN+"成功创建空岛！");
            }else{
                sendChatMessage(player,response.getMessage(), MessageType.ERROR);
            }

        });
        return 1;
    }

}
