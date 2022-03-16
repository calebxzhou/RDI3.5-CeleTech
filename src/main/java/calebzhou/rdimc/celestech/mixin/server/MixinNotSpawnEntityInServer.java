package calebzhou.rdimc.celestech.mixin.server;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LocalMobCapCalculator;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerChunkCache.class)
public class MixinNotSpawnEntityInServer {
    //不在服务端执行刷怪逻辑
    @Redirect(method = "Lnet/minecraft/server/world/ServerChunkManager;tickChunks()V",
    at=@At(target = "Lnet/minecraft/world/SpawnHelper;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/SpawnHelper$Info;ZZZ)V",
    value = "INVOKE"))
    private void noSpawnOnServer(ServerLevel world, LevelChunk chunk, NaturalSpawner.SpawnState info, boolean spawnAnimals, boolean spawnMonsters, boolean rareSpawn){

    }
    @Redirect(method = "Lnet/minecraft/server/world/ServerChunkManager;tickChunks()V",
    at = @At(target = "Lnet/minecraft/world/SpawnHelper;setupSpawn(ILjava/lang/Iterable;Lnet/minecraft/world/SpawnHelper$ChunkSource;Lnet/minecraft/world/SpawnDensityCapper;)Lnet/minecraft/world/SpawnHelper$Info;",
    value = "INVOKE"))
    private NaturalSpawner.SpawnState noSpawnSetup(int spawningChunkCount, Iterable<Entity> entities, NaturalSpawner.ChunkGetter chunkSource, LocalMobCapCalculator spawnDensityCapper){

        return null;
    }
}
