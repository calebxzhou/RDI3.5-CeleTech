package calebzhou.rdimc.celestech.event.impl;


import calebzhou.rdimc.celestech.constant.WorldConstants;
import calebzhou.rdimc.celestech.event.PlayerBreakBlockCallback;
import calebzhou.rdimc.celestech.event.PlayerPlaceBlockCallback;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.cache.LavaStoneCache;
import calebzhou.rdimc.celestech.model.record.BlockRecord2;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

//实体与方块交互的相关事件
public class PlayerBlockEvent {
    public PlayerBlockEvent(){
        PlayerBreakBlockCallback.EVENT.register(((player, blockPos, blockState) -> {
            record(player,blockPos,blockState,BlockRecord2.Action.BREAK);
            //如果玩家破坏了刷石机生成的石头
            if((blockState.getBlock()== Blocks.STONE || blockState.getBlock()== Blocks.COBBLESTONE)
                    && LavaStoneCache.instance.getMap().get(blockPos)!=null){

                //TODO 加经验 随机爆物品
                LavaStoneCache.instance.getMap().remove(blockPos);
                player.experienceProgress += 0.05f;

            }
            return ActionResult.PASS;
        }));
        PlayerPlaceBlockCallback.EVENT.register(((player, blockPos, blockState) -> {
            record(player,blockPos,blockState,BlockRecord2.Action.PLACE);
            return ActionResult.PASS;
        }));
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
            BlockState blockState = world.getBlockState(hitResult.getBlockPos());
            //如果是树苗
            if(blockState.getBlock() instanceof SaplingBlock)
                new SaplingEvents(player,hitResult.getBlockPos(),blockState, ((SaplingBlock) blockState.getBlock()));

            return ActionResult.PASS;
        }));
    }
    private void record(Entity entity,BlockPos blockPos,BlockState blockState,BlockRecord2.Action action){
        String dimension=entity.world.getDimension().getEffects().toString();
        if(!dimension.equals(WorldConstants.OVERWORLD))
            return;
        int posX=blockPos.getX();
        int posY=blockPos.getY();
        int posZ=blockPos.getZ();
        if(posY==0 && posX==0 && posZ==0)
            return;
        CoordLocation location =new CoordLocation(dimension,posX,posY,posZ);
        String playerUuid=entity instanceof PlayerEntity ?entity.getUuidAsString() : entity.getEntityName();
        String blockType=blockState.getBlock().getTranslationKey();

        BlockRecord2 record=new BlockRecord2(playerUuid,blockType,action,location);
        HttpUtils.postObject(record);
    }

}