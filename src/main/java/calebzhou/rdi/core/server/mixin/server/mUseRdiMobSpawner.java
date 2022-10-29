package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.misc.RdiMobSpawner;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.entity.EntityLookup;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Created  on 2022-10-24,16:33.
 */
@Mixin(NaturalSpawner.class)
public class mUseRdiMobSpawner {
	@Overwrite
	public static void spawnForChunk(
			ServerLevel level, LevelChunk chunk, NaturalSpawner.SpawnState spawnState, boolean spawnFriendlies, boolean spawnMonsters, boolean forcedDespawn
	) {
		RdiMobSpawner.tick(level, chunk, forcedDespawn);
	}
}
