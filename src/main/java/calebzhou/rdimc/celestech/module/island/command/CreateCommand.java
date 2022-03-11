package calebzhou.rdimc.celestech.module.island.command;

import calebzhou.rdimc.celestech.command.BaseCommand;
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

public class CreateCommand extends BaseCommand {
    public CreateCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player,String arg) {
        ApiResponse<Island> response = HttpUtils.sendRequestV2("POST", "v2/island/" + player.getUuidAsString());
        sendChatMessage(player,"即将开始新的旅程，请稍等", MessageType.INFO);
        if (response.getType().equals("success")) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,20*30,1));
            Island island = response.getData(Island.class);
            CoordLocation iloca = CoordLocation.fromString(island.getLocation());

            PlayerUtils.teleport(player, iloca.add(0.5, 12, 0.5));
            PlayerUtils.placeBlock(player.getWorld(), iloca, Blocks.OBSIDIAN.getDefaultState());
            PlayerUtils.placeBlock(player.getWorld(), iloca.add(-1,0,0), Blocks.GRASS_BLOCK.getDefaultState());
            PlayerUtils.givePlayerInitialKit(player);
            sendChatMessage(player,"请至公告群中查看教程。",MessageType.INFO);
        }
        sendChatMessage(player, response);
    }

}
