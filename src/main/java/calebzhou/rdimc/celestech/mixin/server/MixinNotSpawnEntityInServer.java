package calebzhou.rdimc.celestech.mixin.server;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnDensityCapper;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerChunkManager.class)
public class MixinNotSpawnEntityInServer {
    //不在服务端执行刷怪逻辑
    @Redirect(method = "Lnet/minecraft/server/world/ServerChunkManager;tickChunks()V",
    at=@At(target = "Lnet/minecraft/world/SpawnHelper;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/SpawnHelper$Info;ZZZ)V",
    value = "INVOKE"))
    private void noSpawnOnServer(ServerWorld world, WorldChunk chunk, SpawnHelper.Info info, boolean spawnAnimals, boolean spawnMonsters, boolean rareSpawn){

    }
    @Redirect(method = "Lnet/minecraft/server/world/ServerChunkManager;tickChunks()V",
    at = @At(target = "Lnet/minecraft/world/SpawnHelper;setupSpawn(ILjava/lang/Iterable;Lnet/minecraft/world/SpawnHelper$ChunkSource;Lnet/minecraft/world/SpawnDensityCapper;)Lnet/minecraft/world/SpawnHelper$Info;",
    value = "INVOKE"))
    private SpawnHelper.Info noSpawnSetup(int spawningChunkCount, Iterable<Entity> entities, SpawnHelper.ChunkSource chunkSource, SpawnDensityCapper spawnDensityCapper){

        return null;
    }
}
