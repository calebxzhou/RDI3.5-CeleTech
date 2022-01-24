package calebzhou.rdimc.celestech.mixin;

import calebzhou.rdimc.celestech.utils.ServerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Mixin(World.class)
public abstract class AntiCrashMixin {
    private static final int ENTITY_TICK_LIMIT = 35;

    @Shadow @Final protected static Logger LOGGER;

    @Redirect(method = "Lnet/minecraft/world/World;tickEntity(Ljava/util/function/Consumer;Lnet/minecraft/entity/Entity;)V",
            at = @At(value = "INVOKE",
            target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    private void tickEntity(Consumer tickConsumer, Object entity){
        try {
            Entity ent = (Entity) entity;
            long t1=System.currentTimeMillis();
            tickConsumer.accept(entity);
            long t2=System.currentTimeMillis();
            long dt=t2-t1;
            if(dt>ENTITY_TICK_LIMIT){
                if(ent instanceof PlayerEntity){
                    return;
                }
                if(ent instanceof VillagerEntity) {
                    return;
                }
                if(ent instanceof AnimalEntity) {
                    return;
                }
                if(ent instanceof ItemEntity ite){
                    if(ite.getStack().hasNbt())
                        return;
                }
                if(ent instanceof MobEntity mobEnt) {
                    if(mobEnt.isPersistent())
                        return;
                }
                if(ent instanceof MinecartEntity){
                    return;
                }
                ent.remove(Entity.RemovalReason.DISCARDED);
            }
        } catch (Throwable var6) {
            LOGGER.error(var6.getMessage());
        }
    }

    @Redirect(
            method = "Lnet/minecraft/world/World;updateNeighbor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;neighborUpdate(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;Z)V"
            )
    )
    private void updateNeigh(BlockState blockState, World world, BlockPos pos, Block sourceBlock, BlockPos neighborPos, boolean b){
        try {
            blockState.neighborUpdate((World)((Object) this), pos, sourceBlock, neighborPos, false);
        } catch (Throwable e) {
            LOGGER.error(e.getMessage());
        }
    }

    //最重要的部分，防止机器卡顿
    @Shadow @Final @Mutable
    protected List<BlockEntityTickInvoker> blockEntityTickers;

    @Shadow public abstract boolean breakBlock(BlockPos pos, boolean drop, @Nullable Entity breakingEntity, int maxUpdateDepth);

    private final HashMap<BlockPos,String> frozenBlockEntityMap = new HashMap<>();
    private final HashMap<BlockPos,Integer> frozenBlockEntityTimeMap = new HashMap<>();

    private static int BLOCKENTITY_TICK_LIMIT = 40;//ms 方块实体40ms没tick完就直接冻结
    private static int BLOCKENTITY_TICK_FROZEN = 50;//冻结50个tick(2.5s)

    @Redirect(method = "tickBlockEntities()V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/BlockEntityTickInvoker;tick()V"))
    private void tickBlockEntity(BlockEntityTickInvoker blockEntityTickInvoker){
        try {
            double mspt=ServerUtils.getMillisecondPerTick();
            String name = blockEntityTickInvoker.getName();
            BlockPos bpos = blockEntityTickInvoker.getPos();
            boolean explicit = false;
            if(mspt>100){
                BLOCKENTITY_TICK_LIMIT=10;
                BLOCKENTITY_TICK_FROZEN=300;
                explicit=true;
            }else if(mspt>60){
                BLOCKENTITY_TICK_LIMIT=15;
                BLOCKENTITY_TICK_FROZEN=150;
                explicit=true;
            }else
            if(mspt>50){
                BLOCKENTITY_TICK_LIMIT=25;
                BLOCKENTITY_TICK_FROZEN=70;
            }
            /*if(size>4096){
                BLOCKENTITY_TICK_LIMIT=20;
                BLOCKENTITY_TICK_FROZEN=200;
            }
            if(size >1024){
                BLOCKENTITY_TICK_LIMIT=30;
                BLOCKENTITY_TICK_FROZEN=100;
            }*/

            //冻结时间 ++, 如果被冻结
            if(frozenBlockEntityMap.containsKey(bpos)){
                Integer frozenTime = frozenBlockEntityTimeMap.get(bpos);
                //达到指定时间以后就解冻
                if(frozenTime>=BLOCKENTITY_TICK_FROZEN){
                    frozenBlockEntityTimeMap.remove(bpos);
                    frozenBlockEntityMap.remove(bpos);
                }
                frozenBlockEntityTimeMap.put(bpos,++frozenTime);
            }else{//如果没被冻结
                if(explicit){
                    //直接删除tick
                    // blockEntityTickers.remove(blockEntityTickInvoker);
                    /*World world = (World) (Object) this;
                    BlockState blockState = world.getBlockState(bpos);
                    world.getChunk(bpos).
                    ItemStack defaultStack = blockState.getBlock().asItem().getDefaultStack();
                    world.setBlockState(bpos, Blocks.AIR.getDefaultState());
                    world.spawnEntity(new ItemEntity(world,bpos.getX(),bpos.getY(),bpos.getZ(),defaultStack));*/
                   // if(RandomUtils.nextInt(0,5)>0)
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

            }


        } catch (Throwable e) {
            LOGGER.error(e.getCause()+e.getMessage());
        }
    }
}
@Mixin(ServerChunkManager.class)
class AntiChunkCrash{
    @Shadow @Final
    private ChunkTicketManager ticketManager;
    @Shadow @Final
    public ThreadedAnvilChunkStorage threadedAnvilChunkStorage;
    @Redirect(method = "Lnet/minecraft/server/world/ServerChunkManager;tick()Z",
    at=@At(value = "INVOKE",target = "Lnet/minecraft/server/world/ChunkTicketManager;tick(Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;)Z"))
    private boolean nocrashTick(ChunkTicketManager instance, ThreadedAnvilChunkStorage chunkStorage){
        try{
            return this.ticketManager.tick(this.threadedAnvilChunkStorage);
        }catch (Throwable t){
            LogManager.getLogger().error(t.toString());
        }
        return false;
    }

}