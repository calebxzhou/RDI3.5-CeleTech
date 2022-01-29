package calebzhou.rdimc.celestech.mixin;

import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.event.PlayerDeathCallback;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerDeathMixin {

    @Shadow public abstract void playerTick();

    @Shadow public abstract void playSound(SoundEvent event, SoundCategory category, float volume, float pitch);

    @Inject(method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V",
    at = @At(
            value = "HEAD"
    ), cancellable = true)
    private void onDeath(DamageSource source,CallbackInfo info){
        ServerPlayerEntity player = (ServerPlayerEntity) (Object)this;
        ActionResult result = PlayerDeathCallback.EVENT.invoker().call(player, source);
        if(result == ActionResult.FAIL)
            info.cancel();
        handleDeath(player);


    }
    private void handleDeath(ServerPlayerEntity player){
        int dropSlotAmount;
        if(PlayerUtils.getDimensionName(player).equals("minecraft:the_end")){
            dropSlotAmount=35;
        }else if(PlayerUtils.getDimensionName(player).equals("minecraft:the_nether")){
            dropSlotAmount=25;
        }else{
            dropSlotAmount=7;
        }
        boolean isSetChestSuccessful;
        World world=player.getWorld();
        BlockPos chestPos = player.getBlockPos();
        isSetChestSuccessful = world.setBlockState(chestPos,Blocks.CHEST.getDefaultState());
        ArrayList<ItemStack> dropItemList = new ArrayList<>();
        for(int i=0;i<dropSlotAmount;i++){
            int ran= RandomUtils.nextInt(1,35);
            ItemStack stack2Drop = player.getInventory().getStack(ran);
            if(stack2Drop.isEmpty() || stack2Drop.getItem() == Items.AIR)
                continue;
            dropItemList.add(stack2Drop);
            player.getInventory().removeOne(stack2Drop);
        }
        BlockEntity chest = world.getBlockEntity(chestPos);

        if(chest instanceof ChestBlockEntity chs && chest!=null){
            isSetChestSuccessful = true;
            for (int i = 0; i < dropItemList.size(); i++) {
                chs.setStack(i,dropItemList.get(i));
            }
        }else{
            isSetChestSuccessful = false;
        }

        if(!isSetChestSuccessful){
            dropItemList.forEach(stack2Drop -> world.spawnEntity(new ItemEntity(world,player.getX()+0.5f,
                    player.getY()+1.1f,player.getZ()+0.5f,
                    stack2Drop)));
        }
    }
}
