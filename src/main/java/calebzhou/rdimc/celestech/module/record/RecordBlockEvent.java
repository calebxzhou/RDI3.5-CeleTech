package calebzhou.rdimc.celestech.module.record;

import calebzhou.rdimc.celestech.api.CallbackRegisterable;
import calebzhou.rdimc.celestech.event.PlayerBreakBlockCallback;
import calebzhou.rdimc.celestech.event.PlayerPlaceBlockCallback;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.record.BlockRecord;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.IdentifierUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

//方块放置、破坏的记录
public class RecordBlockEvent implements CallbackRegisterable {
    public RecordBlockEvent() {
            }

    private InteractionResult record(Entity entity, BlockPos blockPos, BlockState blockState, BlockRecord.Action action){
        String dimension=entity.level.dimensionType().effectsLocation().toString();

        int posX=blockPos.getX();
        int posY=blockPos.getY();
        int posZ=blockPos.getZ();
        if(posY==0 && posX==0 && posZ==0)
            return InteractionResult.PASS;
        CoordLocation location =new CoordLocation(dimension,posX,posY,posZ);
        String playerUuid=entity instanceof Player ?entity.getStringUUID() : entity.getScoreboardName();
        String blockType=blockState.getBlock().getDescriptionId();

        BlockRecord record=new BlockRecord(playerUuid,blockType,action,location);
        HttpUtils.asyncSendObject(record);
        return InteractionResult.PASS;
    }

    @Override
    public void registerCallbacks() {
        PlayerBreakBlockCallback.EVENT.register(IdentifierUtils.byClass(this.getClass()),(player, blockPos, blockState) -> {
            //如果玩家破坏了石头 不记录
            if(blockState.getBlock()== Blocks.STONE || blockState.getBlock()== Blocks.COBBLESTONE){
                return InteractionResult.PASS;
            }
            return record(player,blockPos,blockState, BlockRecord.Action.BREAK);
        });
        PlayerPlaceBlockCallback.EVENT.register(IdentifierUtils.byClass(this.getClass()),((player, blockPos, blockState) -> record(player,blockPos,blockState, BlockRecord.Action.PLACE)));

    }
}
