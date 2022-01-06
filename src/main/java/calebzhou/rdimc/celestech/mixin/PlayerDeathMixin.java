package calebzhou.rdimc.celestech.mixin;

import calebzhou.rdimc.celestech.event.PlayerDeathCallback;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerDeathMixin {

    @Inject(method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V",
    at = @At(
            value = "HEAD"
    ), cancellable = true)
    private void onDeath(DamageSource source,CallbackInfo info){

            ServerPlayerEntity player = (ServerPlayerEntity) (Object)this;
            ActionResult result = PlayerDeathCallback.EVENT.invoker().call(player, source);
            if(result == ActionResult.FAIL)
                info.cancel();

            int dropSlotAmount;
            if(PlayerUtils.getDimensionName(player).equals("minecraft:the_end")){
                dropSlotAmount=30;
            }else if(PlayerUtils.getDimensionName(player).equals("minecraft:the_nether")){
                dropSlotAmount=20;
            }else{
                dropSlotAmount=10;
            }
            for(int i=0;i<dropSlotAmount;i++){
                int ran= RandomUtils.nextInt(1,35);
                ItemStack stack2Drop = player.getInventory().getStack(ran);
                if(stack2Drop.isEmpty())
                    continue;
                player.getInventory().removeOne(stack2Drop);
                World w=player.getWorld();
                w.spawnEntity(new ItemEntity(w,player.getX()+0.5f,
                        player.getY()+1.1f,player.getZ()+0.5f,
                        stack2Drop));
                //String itemNbt = stack2Drop.serializeNBT().toString();
            }


    }
}
