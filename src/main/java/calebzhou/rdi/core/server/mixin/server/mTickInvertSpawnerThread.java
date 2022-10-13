package calebzhou.rdi.core.server.mixin.server;

import calebzhou.rdi.core.server.mixin.AccessChunkMap;
import calebzhou.rdi.core.server.thread.MobSpawningThread;
import calebzhou.rdi.core.server.utils.ThreadPool;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LocalMobCapCalculator;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.LevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Created by calebzhou on 2022-10-06,9:52.
 */
@Mixin(ServerChunkCache.class)
public abstract class mTickInvertSpawnerThread {
	@Shadow
	protected abstract void getFullChunk(long chunkPos, Consumer<LevelChunk> chunkConsumer);

	@Shadow
	@Final
	private ServerLevel level;

	@Shadow
	@Final
	public ChunkMap chunkMap;

	@Shadow
	private boolean spawnEnemies;

	@Shadow
	private boolean spawnFriendlies;

	@Redirect(method = "tickChunks",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/level/NaturalSpawner;createState(ILjava/lang/Iterable;Lnet/minecraft/world/level/NaturalSpawner$ChunkGetter;Lnet/minecraft/world/level/LocalMobCapCalculator;)Lnet/minecraft/world/level/NaturalSpawner$SpawnState;"))
	private NaturalSpawner.SpawnState noVanillaCreateSpawnState(int spawnableChunkCount, Iterable<Entity> entities, NaturalSpawner.ChunkGetter chunkGetter, LocalMobCapCalculator calculator){
		return null;
	}
	@Redirect(method = "tickChunks",at=@At(value = "INVOKE",target = "Lnet/minecraft/world/level/NaturalSpawner;spawnForChunk(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/LevelChunk;Lnet/minecraft/world/level/NaturalSpawner$SpawnState;ZZZ)V"))
	private void noVanillaSpawnMob(ServerLevel level, LevelChunk chunk, NaturalSpawner.SpawnState spawnState, boolean spawnFriendlies, boolean spawnMonsters, boolean forcedDespawn){

	}
	@Inject(method = "tickChunks",at=@At(value = "INVOKE",target = "Ljava/util/Collections;shuffle(Ljava/util/List;)V",shift = At.Shift.AFTER),locals = LocalCapture.CAPTURE_FAILSOFT)
	private void addSpawnMobTask(CallbackInfo ci, long l, long m, boolean bl, LevelData levelData, ProfilerFiller profilerFiller, int i, boolean bl2, int j, NaturalSpawner.SpawnState spawnState, List<ServerChunkCache.ChunkAndHolder> list, boolean bl3){

		MobSpawningThread.addTask(()->{
			CopyOnWriteArrayList<ServerChunkCache.ChunkAndHolder> writeArrayList = new CopyOnWriteArrayList<>(list);
			NaturalSpawner.SpawnState state = NaturalSpawner.createState(
					j, this.level.getAllEntities(), this::getFullChunk, new LocalMobCapCalculator(this.chunkMap)
			);
			for(ServerChunkCache.ChunkAndHolder chunkAndHolder : writeArrayList) {
				LevelChunk levelChunk2 = chunkAndHolder.chunk();
				ChunkPos chunkPos = levelChunk2.getPos();
				if (this.level.isNaturalSpawningAllowed(chunkPos) && ((AccessChunkMap)this.chunkMap).invokeAnyPlayerCloseEnoughForSpawning(chunkPos)) {
					levelChunk2.incrementInhabitedTime(m);
					if (bl3 && (this.spawnEnemies || this.spawnFriendlies) && this.level.getWorldBorder().isWithinBounds(chunkPos)) {
						NaturalSpawner.spawnForChunk(this.level, levelChunk2, state, this.spawnFriendlies, this.spawnEnemies, bl2);//bl2：到达20秒
					}

					if (this.level.shouldTickBlocksAt(chunkPos.toLong())) {
						this.level.tickChunk(levelChunk2, i);
					}
				}
			}
		});

	}
}
