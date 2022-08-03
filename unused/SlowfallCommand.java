package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class SlowfallCommand extends BaseCommand {

    public SlowfallCommand(String command, int permissionLevel) {
        super(command, permissionLevel,false);
    }
    @Override
    protected void onExecute(ServerPlayer player, String arg) {
        int level=0;
        try {
            level = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            e=null;
            return;
        }
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,20*10,level-1));
    }


}
