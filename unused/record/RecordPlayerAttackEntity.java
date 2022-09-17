package calebzhou.rdi.celestech.module.record;

import calebzhou.rdi.celestech.api.CallbackRegisterable;
import calebzhou.rdi.celestech.utils.HttpUtils;
import calebzhou.rdi.celestech.utils.IdentifierUtils;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class RecordPlayerAttackEntity implements CallbackRegisterable {
    public RecordPlayerAttackEntity() {

    }
    private void insertMobAttackRecord(Player player, LivingEntity entity){
        HttpUtils.sendRequestV2("POST","v2/mob_attack");
        /*EntityType<? extends LivingEntity> type = (EntityType<? extends LivingEntity>) entity.getType();
        double mobDamage = DefaultAttributeRegistry.get(type).getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        double mobHp = DefaultAttributeRegistry.get(type).getValue(EntityAttributes.GENERIC_MAX_HEALTH);*/

    }

    @Override
    public void registerCallbacks() {
        AttackEntityCallback.EVENT.register(IdentifierUtils.byClass(this.getClass()),(player, world, hand, entity, hitResult) -> {
            if(entity instanceof LivingEntity livingEntity){
                insertMobAttackRecord(player,livingEntity);
                return InteractionResult.PASS;
            }

            return InteractionResult.PASS;
        });
    }
}
