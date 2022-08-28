package calebzhou.rdimc.celestech.mixin.gameplay;


import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Criterion;
import net.minecraft.server.PlayerAdvancements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(PlayerAdvancements.class)
public class mNoStackOverflowReadAdvancements {
    @Redirect(method = "startProgress",at = @At(value = "INVOKE",target = "Lnet/minecraft/advancements/AdvancementProgress;update(Ljava/util/Map;[[Ljava/lang/String;)V"))
    private void noStackOverflowReadAdvancements(AdvancementProgress instance, Map<String, Criterion> advancement, String[][] advancementProgress){
        try {
            instance.update(advancement, advancementProgress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
