package calebzhou.rdimc.celestech.model.thread;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.constant.WorldConstants;
import calebzhou.rdimc.celestech.enums.ColorConst;
import calebzhou.rdimc.celestech.model.PlayerMotionPath;
import calebzhou.rdimc.celestech.model.PlayerTemperature;
import calebzhou.rdimc.celestech.utils.*;
import io.netty.util.internal.MathUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static calebzhou.rdimc.celestech.model.PlayerTemperature.*;

public class PlayerTemperatureThread extends PlayerBaseThread{

    public PlayerTemperatureThread(ServerPlayerEntity player){
        super(player.getDisplayName().getString());
    }

    protected void execute() throws InterruptedException{
        ServerWorld world = player.getWorld();
        BlockPos camPos = player.getCameraBlockPos();
        BlockPos footPos = player.getLandingPos();
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
            TextUtils.sendActionMessage(player,String.format(ColorConst.GOLD+"%.2f℃",PlayerTemperature.get(playerName)));
        }
        if(temp > TEMP_DANGER && temp < TEMP_DANGER2){
            player.damage(DamageSource.HOT_FLOOR,0.1f);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,20,1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,20,1));
            TextUtils.sendActionMessage(player,String.format(ColorConst.RED+ColorConst.ITALIC+"咋这么迷糊呢",PlayerTemperature.get(playerName)));
        }
        if(temp >= TEMP_DANGER2 && temp < TEMP_DANGER3){
            player.damage(DamageSource.HOT_FLOOR,1.0f);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,20,1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,40,2));
            TextUtils.sendActionMessage(player,String.format(ColorConst.RED+ColorConst.ITALIC+"咋这么迷糊呢",PlayerTemperature.get(playerName)));
        }
        if(temp >= TEMP_DANGER3 && temp < PlayerTemperature.TEMP_DANGER4){
            player.damage(DamageSource.HOT_FLOOR,2.0f);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,20,2));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,60,3));
            TextUtils.sendActionMessage(player,String.format(ColorConst.RED+ColorConst.ITALIC+"咋这么迷糊呢",PlayerTemperature.get(playerName)));
        }
        if(temp >= PlayerTemperature.TEMP_DANGER4 && temp < PlayerTemperature.TEMP_MAX){
            player.damage(DamageSource.HOT_FLOOR,3.0f);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,20,3));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,60,4));
            TextUtils.sendActionMessage(player,String.format(ColorConst.RED+ColorConst.ITALIC+"咋这么迷糊呢",PlayerTemperature.get(playerName)));
        }
        if(temp>=PlayerTemperature.TEMP_MAX){
            player.damage(DamageSource.HOT_FLOOR,5.0f);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,20,4));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,60,5));
            TextUtils.sendActionMessage(player,String.format(ColorConst.RED+ColorConst.ITALIC+"咋这么迷糊呢",PlayerTemperature.get(playerName)));
        }
    }

}
