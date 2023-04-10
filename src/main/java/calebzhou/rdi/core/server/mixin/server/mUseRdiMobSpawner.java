package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.misc.ServerLaggingStatus;
import calebzhou.rdi.core.server.utils.ServerUtils;
import calebzhou.rdi.core.server.utils.WorldUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created  on 2022-10-24,16:33.
 */
@Mixin(NaturalSpawner.class)
public class mUseRdiMobSpawner {
	@Inject(method = "spawnForChunk",at=@At("HEAD"), cancellable = true)
	private static void spawn4chunk(ServerLevel level, LevelChunk chunk, NaturalSpawner.SpawnState spawnState, boolean spawnFriendlies, boolean spawnMonsters, boolean forcedDespawn, CallbackInfo ci){
		if(level == RdiCoreServer.getServer().overworld())
			ci.cancel();
		if(ServerLaggingStatus.isServerVeryLagging())
			ci.cancel();
	}

}
