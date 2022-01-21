package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.NoArgCommand;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class CreateCommand extends NoArgCommand {
    public CreateCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player) {
        ApiResponse<Island> response = HttpUtils.sendRequest("POST", "island/" + player.getUuidAsString());
        if (response.getType().equals("success")) {
            Island island = response.getData(Island.class);
            CoordLocation iloca = CoordLocation.fromString(island.getLocation());
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,20*10,1));
            PlayerUtils.teleport(player, iloca.add(0.5, 6, 0.5));
            PlayerUtils.placeBlock(player.getWorld(), iloca, "minecraft:obsidian");
            PlayerUtils.givePlayerInitialKit(player);
        }
        sendChatMessage(player, response);
    }

}
