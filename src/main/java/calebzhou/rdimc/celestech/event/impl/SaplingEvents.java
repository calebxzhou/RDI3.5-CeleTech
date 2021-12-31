package calebzhou.rdimc.celestech.event.impl;

import calebzhou.rdimc.celestech.constant.AngrySaplingDamageSource;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.SaplingBlock;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.Explosion;

public class SaplingEvents {
    PlayerEntity player;
    BlockPos blockPos;
    BlockState blockState;
    SaplingBlock block;

    public SaplingEvents(PlayerEntity player, BlockPos blockPos, BlockState blockState,SaplingBlock block) {
        this.player = player;
        this.blockPos = blockPos;
        this.blockState = blockState;
        this.block = block;
        //按下shift键
        if(player.isSneaking()){
            grow();
            //血量大于8
            if(player.getHealth()>8)
                player.setHealth(0.1f);
            else{
                Explosion exp = new Explosion(player.getWorld(),player, player.getX(), player.getY(), player.getZ(),2.0f);
                player.damage(DamageSource.explosion(exp),0.1f);
                player.damage(AngrySaplingDamageSource.source,5.0f);

            }
        }
    }

    public void grow(){
        block.grow(((ServerWorld) player.getWorld()),player.getRandom(),blockPos,blockState);
    }
}
