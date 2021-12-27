package calebzhou.rdimc.celestech.event;


import calebzhou.rdimc.celestech.model.BlockRecord;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.TimeUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
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
    }
    private static void record(Entity entity,BlockPos blockPos,BlockState blockState,BlockAction blockAction){
        int posX=blockPos.getX();
        int posY=blockPos.getY();
        int posZ=blockPos.getZ();
        if(posY==0 && posX==0 && posZ==0)
            return;
        String playerUuid=entity instanceof PlayerEntity ?entity.getUuidAsString() : entity.getDisplayName().getString();
        String blockType=blockState.getBlock().getLootTableId().toString();
        String dimension=entity.world.getDimension().getEffects().toString();
        BlockRecord record=new BlockRecord(playerUuid,blockType,blockAction.toString(),dimension,posX,posY,posZ, TimeUtils.getNow());
        HttpUtils.postObject(record);
    }
}
enum BlockAction{
    PLACE,BREAK
}