package calebzhou.rdimc.celestech.module.record;

import calebzhou.rdimc.celestech.api.CallbackRegisterable;
import calebzhou.rdimc.celestech.event.PlayerBreakBlockCallback;
import calebzhou.rdimc.celestech.event.PlayerPlaceBlockCallback;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.record.BlockRecord;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

//方块放置、破坏的记录
public class RecordBlockEvent implements CallbackRegisterable {
    public RecordBlockEvent() {
            }

    private ActionResult record(Entity entity, BlockPos blockPos, BlockState blockState, BlockRecord.Action action){
        String dimension=entity.world.getDimension().getEffects().toString();

        int posX=blockPos.getX();
        int posY=blockPos.getY();
        int posZ=blockPos.getZ();
        if(posY==0 && posX==0 && posZ==0)
            return ActionResult.PASS;
        CoordLocation location =new CoordLocation(dimension,posX,posY,posZ);
        String playerUuid=entity instanceof PlayerEntity ?entity.getUuidAsString() : entity.getEntityName();
        String blockType=blockState.getBlock().getTranslationKey();

        BlockRecord record=new BlockRecord(playerUuid,blockType,action,location);
        HttpUtils.asyncSendObject(record);
        return ActionResult.SUCCESS;
    }

    @Override
    public void registerCallbacks() {
        PlayerBreakBlockCallback.EVENT.register(((player, blockPos, blockState) -> {
            //如果玩家破坏了石头 不记录
            if(blockState.getBlock()== Blocks.STONE || blockState.getBlock()== Blocks.COBBLESTONE){
                return ActionResult.PASS;
            }
            return record(player,blockPos,blockState, BlockRecord.Action.BREAK);
        }));
        PlayerPlaceBlockCallback.EVENT.register(((player, blockPos, blockState) -> record(player,blockPos,blockState, BlockRecord.Action.PLACE)));

    }
}
