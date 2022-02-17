package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.constant.WorldConstants;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

public class SlowfallCommand extends BaseCommand {

    public SlowfallCommand(String command, int permissionLevel) {
        super(command, permissionLevel,false);
    }
    @Override
    protected void onExecute(ServerPlayerEntity player, String arg) {
        int level=0;
        try {
            level = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            TextUtils.sendChatMessage(player,"数字格式错误!!", MessageType.ERROR);
            return;
        }
        if(!PlayerUtils.getDimensionName(player).equals(WorldConstants.DEFAULT_WORLD)){
            TextUtils.sendChatMessage(player,"这个世界太沉了呀");
            return ;
        }
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,20*10,level-1));
    }


}
