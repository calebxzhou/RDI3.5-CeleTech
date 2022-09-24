package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.ServerStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by calebzhou on 2022-09-24,23:17.
 */
@Mixin(NaturalSpawner.class)
public class mNaturalSpawner {
	@Inject(method = "spawnForChunk",at=@At("HEAD"),cancellable = true)
	private static void RdiSpawnWithTpsCheck(ServerLevel level, LevelChunk chunk, NaturalSpawner.SpawnState spawnState, boolean spawnFriendlies, boolean spawnMonsters, boolean forcedDespawn, CallbackInfo ci){
		if(ServerStatus.worseThan20Tps())
			ci.cancel();
	}
}
