package calebzhou.rdi.core.server.mixin.mt;

import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
import net.himeki.mcmtfabric.parallelised.fastutil.ConcurrentLongLinkedOpenHashSet;
import net.himeki.mcmtfabric.parallelised.fastutil.Long2ByteConcurrentHashMap;
import net.minecraft.world.level.lighting.DynamicGraphMinFixedPoint;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DynamicGraphMinFixedPoint.class)
public abstract class LevelPropagatorMixin {
	@Mutable
	@Shadow
	@Final
	private LongLinkedOpenHashSet[] queues;

	@Shadow
	@Final
	@Mutable
	private Long2ByteMap computedLevels;

	@Inject(method = "<init>",at=@At(value = "TAIL"))
	private void asd(int i, int j, int k, CallbackInfo ci){
		queues=new ConcurrentLongLinkedOpenHashSet[i];
		for(int l = 0; l < i; ++l) {
			this.queues[l] = new ConcurrentLongLinkedOpenHashSet(j, 0.5F) {
				@Override
				protected void rehash(int i) {
					if (i > j) {
						super.rehash(i);
					}

				}
			};
		}
	}
    /*@Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/lighting/DynamicGraphMinFixedPoint;queues:[Lit/unimi/dsi/fastutil/longs/LongLinkedOpenHashSet;", args = "array=set"))
    private void overwritePendingIdUpdatesByLevel(LongLinkedOpenHashSet[] hashSets, int index, LongLinkedOpenHashSet hashSet, int levelCount, final int expectedLevelSize, final int expectedTotalSize) {
        hashSets[index] = new ConcurrentLongLinkedOpenHashSet(expectedLevelSize, 0.5f);
    }
    @Redirect(method = "<init>", at = @At(ordinal = 0,value = "FIELD", target = "Lnet/minecraft/world/level/lighting/DynamicGraphMinFixedPoint;computedLevels:Lit/unimi/dsi/fastutil/longs/Long2ByteMap;"))
    private void overwritePendingI2dUpdatesByLevel(DynamicGraphMinFixedPoint instance, Long2ByteMap value) {
		value=new Long2ByteConcurrentHashMap();
       // hashSets[index] = new ConcurrentLongLinkedOpenHashSet(expectedLevelSize, 0.5f);
    }*/
}
