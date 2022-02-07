package calebzhou.rdimc.celestech.command.impl.island;

import calebzhou.rdimc.celestech.command.NoArgCommand;
import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;
import static calebzhou.rdimc.celestech.utils.TextUtils.sendClickableContent;

public class CreateCommand extends NoArgCommand {
    public CreateCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player) {
        ApiResponse<Island> response = HttpUtils.sendRequest("POST", "island/" + player.getUuidAsString());
        sendChatMessage(player,"开始创建空岛,请您不要触摸键盘 或者 鼠标.", MessageType.INFO);
        if (response.getType().equals("success")) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,20*30,1));
            Island island = response.getData(Island.class);
            CoordLocation iloca = CoordLocation.fromString(island.getLocation());

            PlayerUtils.teleport(player, iloca.add(0.5, 12, 0.5));
            PlayerUtils.placeBlock(player.getWorld(), iloca, Blocks.OBSIDIAN.getDefaultState());
            PlayerUtils.placeBlock(player.getWorld(), iloca.add(-1,0,0), Blocks.GRASS_BLOCK.getDefaultState());
            PlayerUtils.givePlayerInitialKit(player);
            sendChatMessage(player,"请至公告群中查看空岛教程。",MessageType.INFO);
        }
        sendChatMessage(player, response);
    }

}
