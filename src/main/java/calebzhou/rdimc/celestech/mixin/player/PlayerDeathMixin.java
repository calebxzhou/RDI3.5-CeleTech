package calebzhou.rdimc.celestech.mixin.player;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.event.PlayerDeathCallback;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

@Mixin(ServerPlayer.class)
public abstract class PlayerDeathMixin {

    @Inject(method = "Lnet/minecraft/server/level/ServerPlayer;die(Lnet/minecraft/world/damagesource/DamageSource;)V",
    at = @At(
            value = "HEAD"
    ), cancellable = true)
    private void onDeath(DamageSource source,CallbackInfo info){
        ServerPlayer player = (ServerPlayer) (Object)this;
        InteractionResult result = PlayerDeathCallback.EVENT.invoker().call(player, source);
        if(result == InteractionResult.FAIL)
            info.cancel();
        handleDeath(player);


    }
    private void handleDeath(ServerPlayer player){
        int dropSlotAmount;
        if(PlayerUtils.getDimensionName(player).equals("minecraft:the_end")){
            dropSlotAmount=35;
        }else if(PlayerUtils.getDimensionName(player).equals("minecraft:the_nether")){
            dropSlotAmount=25;
        }else{
            dropSlotAmount=7;
        }
        boolean isSetChestSuccessful;
        Level world=player.getLevel();
        BlockPos chestPos = player.blockPosition();
        if(new BoundingBox(-100,-64,-100,100,320,100).isInside(new Vec3i(chestPos.getX(), chestPos.getY(), chestPos.getZ())))
            isSetChestSuccessful = false;
        else
            isSetChestSuccessful = world.setBlockAndUpdate(chestPos,Blocks.CHEST.defaultBlockState());
        ArrayList<ItemStack> dropItemList = new ArrayList<>();
        for(int i=0;i<dropSlotAmount;i++){
            int ran= RDICeleTech.RANDOM.nextInt(1,35);
            ItemStack stack2Drop = player.getInventory().getItem(ran);
            if(stack2Drop.isEmpty() || stack2Drop.getItem() == Items.AIR)
                continue;
            dropItemList.add(stack2Drop);
            player.getInventory().removeItem(stack2Drop);
        }
        BlockEntity chest = world.getBlockEntity(chestPos);

        if(chest instanceof ChestBlockEntity chs && chest!=null){
            isSetChestSuccessful = true;
            for (int i = 0; i < dropItemList.size(); i++) {
                chs.setItem(i,dropItemList.get(i));
            }
        }else{
            isSetChestSuccessful = false;
        }

        if(!isSetChestSuccessful){
            dropItemList.forEach(stack2Drop -> world.addFreshEntity(new ItemEntity(world,player.getX()+0.5f,
                    player.getY()+1.1f,player.getZ()+0.5f,
                    stack2Drop)));
        }
    }
}
