package calebzhou.rdimc.celestech.model.thread;

import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.model.PlayerTemperature;
import calebzhou.rdimc.celestech.utils.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import static calebzhou.rdimc.celestech.model.PlayerTemperature.*;

public class PlayerTemperatureThread extends PlayerBaseThread{

    public PlayerTemperatureThread(ServerPlayer player){
        super(player.getDisplayName().getString());
    }

    protected void execute() throws InterruptedException{
        ServerLevel world = player.getLevel();
        BlockPos camPos = player.eyeBlockPosition();
        BlockPos footPos = player.getOnPos();
        if(!PlayerTemperature.has(playerName)){
            PlayerTemperature.put(playerName,PlayerTemperature.DEFAULT_TEMP);
        }
        //准备变更的体温数值
        float tempToModify=0;
        //1.判断时间
        //如果有太阳
        if(WorldUtils.canPositionSeeSun(world,camPos)){
            tempToModify+=0.01;
        }
        //如果在水里
        if(WorldUtils.isInWater(world,footPos)||player.isSwimming()){
            if(PlayerTemperature.get(playerName)>TEMP_DANGER3)
                tempToModify-=0.08;
            if(PlayerTemperature.get(playerName)>TEMP_DANGER2)
                tempToModify-=0.04;
            if(PlayerTemperature.get(playerName)>TEMP_DANGER)
                tempToModify-=0.025;


            else
                tempToModify-=0.01;
        }
        //如果附近有岩浆
        if(WorldUtils.isNearbyLava(world,camPos))
            tempToModify+=0.1;
        PlayerTemperature.add(playerName,tempToModify);
        checkTemp();

        new PlayerTemperatureThread(player).run();
    }
    private void checkTemp(){
        float temp = PlayerTemperature.get(playerName);
        System.out.println(temp);
        if(temp > PlayerTemperature.TEMP_WARN){
            TextUtils.sendActionMessage(player,String.format(ColorConstants.GOLD+"%.2f℃",PlayerTemperature.get(playerName)));
        }
        if(temp > TEMP_DANGER && temp < TEMP_DANGER2){
            player.hurt(DamageSource.HOT_FLOOR,0.1f);
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,20,1));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION,20,1));
            TextUtils.sendActionMessage(player,String.format(ColorConstants.RED+ ColorConstants.ITALIC+"%.2f℃",PlayerTemperature.get(playerName)));
        }
        if(temp >= TEMP_DANGER2 && temp < TEMP_DANGER3){
            player.hurt(DamageSource.HOT_FLOOR,1.0f);
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,20,1));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION,40,2));
            TextUtils.sendActionMessage(player,String.format(ColorConstants.RED+ ColorConstants.ITALIC+"%.2f℃",PlayerTemperature.get(playerName)));
        }
        if(temp >= TEMP_DANGER3 && temp < PlayerTemperature.TEMP_DANGER4){
            player.hurt(DamageSource.HOT_FLOOR,2.0f);
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,20,2));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION,60,3));
            TextUtils.sendActionMessage(player,String.format(ColorConstants.RED+ ColorConstants.ITALIC+"%.2f℃",PlayerTemperature.get(playerName)));
        }
        if(temp >= PlayerTemperature.TEMP_DANGER4 && temp < PlayerTemperature.TEMP_MAX){
            player.hurt(DamageSource.HOT_FLOOR,3.0f);
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,20,3));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION,60,4));
            TextUtils.sendActionMessage(player,String.format(ColorConstants.RED+ ColorConstants.ITALIC+"%.2f℃",PlayerTemperature.get(playerName)));
        }
        if(temp>=PlayerTemperature.TEMP_MAX){
            player.hurt(DamageSource.HOT_FLOOR,5.0f);
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,20,4));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION,60,5));
            TextUtils.sendActionMessage(player,String.format(ColorConstants.RED+ ColorConstants.ITALIC+"%.2f℃",PlayerTemperature.get(playerName)));
        }
    }

}
