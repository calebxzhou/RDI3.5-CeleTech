package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendClickableContent;

public class CreateCommand extends BaseCommand {
    public CreateCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayer player,String arg) {
        ApiResponse<Island> response = HttpUtils.sendRequestV2("POST", "v2/island/" + player.getStringUUID());
        sendChatMessage(player,"即将开始新的旅程，请稍等", MessageType.INFO);
        if (response.getType().equals("success")) {
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,20*30,1));
            Island island = response.getData(Island.class);
            CoordLocation iloca = CoordLocation.fromString(island.getLocation());

            PlayerUtils.teleport(player, iloca.add(0.5, 12, 0.5));
            PlayerUtils.placeBlock(player.getLevel(), iloca, Blocks.OBSIDIAN.defaultBlockState());
            PlayerUtils.placeBlock(player.getLevel(), iloca.add(-1,0,0), Blocks.GRASS_BLOCK.defaultBlockState());
            PlayerUtils.givePlayerInitialKit(player);
            sendChatMessage(player,"请至公告群中查看教程。",MessageType.INFO);
        }
        sendChatMessage(player, response);
    }

}
