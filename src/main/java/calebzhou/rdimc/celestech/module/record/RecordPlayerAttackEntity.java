package calebzhou.rdimc.celestech.module.record;

import calebzhou.rdimc.celestech.api.CallbackRegisterable;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public class RecordPlayerAttackEntity implements CallbackRegisterable {
    public RecordPlayerAttackEntity() {

    }
    private void insertMobAttackRecord(PlayerEntity player, LivingEntity entity){
        HttpUtils.sendRequestV2("POST","v2/mob_attack");
        /*EntityType<? extends LivingEntity> type = (EntityType<? extends LivingEntity>) entity.getType();
        double mobDamage = DefaultAttributeRegistry.get(type).getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        double mobHp = DefaultAttributeRegistry.get(type).getValue(EntityAttributes.GENERIC_MAX_HEALTH);*/

    }

    @Override
    public void registerCallbacks() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if(entity instanceof LivingEntity livingEntity){
                insertMobAttackRecord(player,livingEntity);
                return ActionResult.SUCCESS;
            }

            return ActionResult.PASS;
        });
    }
}
