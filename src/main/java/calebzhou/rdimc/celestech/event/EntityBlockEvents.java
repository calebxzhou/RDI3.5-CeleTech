package calebzhou.rdimc.celestech.event;


import calebzhou.rdimc.celestech.model.record.BlockRecord;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.TimeUtils;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
//实体与方块交互的相关事件
public class EntityBlockEvents {
    public EntityBlockEvents(){
        PlayerBreakBlockCallback.EVENT.register(((player, blockPos, blockState) -> {
            record(player,blockPos,blockState,BlockAction.BREAK);
            return ActionResult.PASS;
        }));
        PlayerPlaceBlockCallback.EVENT.register(((player, blockPos, blockState) -> {
            record(player,blockPos,blockState,BlockAction.PLACE);
            return ActionResult.PASS;
        }));
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
            BlockState blockState = world.getBlockState(hitResult.getBlockPos());
            //如果不是树苗
            if(!(blockState.getBlock() instanceof SaplingBlock))
                return ActionResult.PASS;
            //接下来，如果是树苗
            new SaplingEvents(player,hitResult.getBlockPos(),blockState, ((SaplingBlock) blockState.getBlock()));

            return ActionResult.PASS;
        }));
    }
    private static void record(Entity entity,BlockPos blockPos,BlockState blockState,BlockAction blockAction){
        int posX=blockPos.getX();
        int posY=blockPos.getY();
        int posZ=blockPos.getZ();
        if(posY==0 && posX==0 && posZ==0)
            return;
        String playerUuid=entity instanceof PlayerEntity ?entity.getUuidAsString() : entity.getDisplayName().getString();
        String blockType=blockState.getBlock().getTranslationKey();
        String dimension=entity.world.getDimension().getEffects().toString();
        BlockRecord record=new BlockRecord(playerUuid,blockType,blockAction.toString(),dimension,posX,posY,posZ, TimeUtils.getNow());
        HttpUtils.postObject(record);
    }

}
enum BlockAction{
    PLACE,BREAK
}