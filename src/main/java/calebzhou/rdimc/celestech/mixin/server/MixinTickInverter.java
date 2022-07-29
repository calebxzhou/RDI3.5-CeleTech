package calebzhou.rdimc.celestech.mixin.server;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.module.ticking.TickInverter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(Level.class)
public abstract class MixinTickInverter {

    private static final int ENTITY_TICK_LIMIT = 35;






    //最重要的部分，防止机器卡顿
    @Shadow @Final @Mutable protected List<TickingBlockEntity> blockEntityTickers;

    @Shadow public abstract boolean destroyBlock(BlockPos blockPos, boolean bl, @Nullable Entity entity, int i);

    @Redirect(method = "tickBlockEntities()V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/TickingBlockEntity;tick()V"))
    private void tickBlockEntity(TickingBlockEntity blockEntityTickInvoker){
        try {
            TickInverter.INSTANCE.tickBlockEntity(blockEntityTickInvoker);
            /*double mspt=ServerUtils.getMillisecondPerTick();
            String name = blockEntityTickInvoker.getName();
            BlockPos bpos = blockEntityTickInvoker.getPos();
            boolean explicit = false;
            if(mspt>100){
                BLOCKENTITY_TICK_LIMIT=10;
                BLOCKENTITY_TICK_FROZEN=80;
                explicit=true;
            }else if(mspt>60){
                BLOCKENTITY_TICK_LIMIT=15;
                BLOCKENTITY_TICK_FROZEN=40;
            }else
            if(mspt>50){
                BLOCKENTITY_TICK_LIMIT=25;
                BLOCKENTITY_TICK_FROZEN=20;
            }
            if(size>4096){
                BLOCKENTITY_TICK_LIMIT=20;
                BLOCKENTITY_TICK_FROZEN=200;
            }
            if(size >1024){
                BLOCKENTITY_TICK_LIMIT=30;
                BLOCKENTITY_TICK_FROZEN=100;
            }

            //冻结时间 ++, 如果被冻结
            if(frozenBlockEntityMap.containsKey(bpos)){
                Integer frozenTime = frozenBlockEntityTimeMap.get(bpos);
                //达到指定时间以后就解冻
                if(frozenTime>=BLOCKENTITY_TICK_FROZEN){
                    frozenBlockEntityTimeMap.remove(bpos);
                    frozenBlockEntityMap.remove(bpos);
                }
                frozenBlockEntityTimeMap.put(bpos,++frozenTime);
            }else{
                if(explicit){
                    //直接删除tick
                     blockEntityTickers.remove(blockEntityTickInvoker);
                    World world = (World) (Object) this;
                    BlockState blockState = world.getBlockState(bpos);
                    world.getChunk(bpos).
                    ItemStack defaultStack = blockState.getBlock().asItem().getDefaultStack();
                    world.setBlockState(bpos, Blocks.AIR.getDefaultState());
                    world.spawnEntity(new ItemEntity(world,bpos.getX(),bpos.getY(),bpos.getZ(),defaultStack));
                   if(RandomUtils.nextInt(0,5)>0)
                        return;
                }

                long t1=System.currentTimeMillis();
                blockEntityTickInvoker.tick();
                long t2=System.currentTimeMillis();

                long dt=t2-t1;
                if(dt > BLOCKENTITY_TICK_LIMIT){
                    frozenBlockEntityMap.put(bpos,name);
                    frozenBlockEntityTimeMap.put(bpos,0);
                }

            }*/


        } catch (Throwable e) {
            RDICeleTech.LOGGER.error(e.getCause()+e.getMessage());
        }
    }
}
