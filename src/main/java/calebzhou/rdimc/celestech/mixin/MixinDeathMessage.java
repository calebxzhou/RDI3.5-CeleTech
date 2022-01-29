package calebzhou.rdimc.celestech.mixin;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageSource.class)
public class MixinDeathMessage {
    @Inject(method = "Lnet/minecraft/entity/damage/DamageSource;getDeathMessage(Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/text/Text;",
    at=@At(value = "RETURN"))
    private void changeMsg(LivingEntity deathEntity, CallbackInfoReturnable<Text> cir){
        LivingEntity attacker = deathEntity.getPrimeAdversary();
        String name = deathEntity.getEntityName();
        if(deathEntity instanceof ZombieEntity)
            cir.setReturnValue(new LiteralText(name+"被僵尸挠死了"));
        else if(deathEntity instanceof CreeperEntity)
            cir.setReturnValue(new LiteralText(name+"被苦力怕崩飞了"));
        else if(deathEntity instanceof GhastEntity)
            cir.setReturnValue(new LiteralText(name+"被恶魂一炮狙送上了天"));
    }
}
