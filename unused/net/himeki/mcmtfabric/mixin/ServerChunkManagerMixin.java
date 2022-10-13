package net.himeki.mcmtfabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.datafixers.util.Either;
import net.himeki.mcmtfabric.DebugHookTerminator;
import net.himeki.mcmtfabric.ParallelProcessor;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Mixin(ServerChunkCache.class)
public abstract class ServerChunkManagerMixin extends ChunkSource {

    @Shadow
    @Final
    public ServerChunkCache.MainThreadExecutor mainThreadExecutor;

    @Shadow
    @Final
    ServerLevel world;

    @Redirect(method = "tickChunks", at = @At(value = "INVOKE", target = "Ljava/util/Collections;shuffle(Ljava/util/List;)V"))
    private void preChunkTick(List<?> list) {
        ParallelProcessor.preChunkTick(this.world);
        Collections.shuffle(list);
    }

    @Redirect(method = "tickChunks", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tickChunk(Lnet/minecraft/world/chunk/WorldChunk;I)V"))
    private void overwriteTickChunk(ServerLevel serverWorld, LevelChunk chunk, int randomTickSpeed) {
        ParallelProcessor.callTickChunks(serverWorld, chunk, randomTickSpeed);
    }


    @Redirect(method = {"getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/Chunk;", "getWorldChunk"}, at = @At(value = "FIELD", target = "Lnet/minecraft/server/world/ServerChunkManager;serverThread:Ljava/lang/Thread;", opcode = Opcodes.GETFIELD))
    private Thread overwriteServerThread(ServerChunkCache mgr) {
        return Thread.currentThread();
    }

    @Redirect(method = "getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/Chunk;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;visit(Ljava/lang/String;)V"))
    private void overwriteProfilerVisit(ProfilerFiller instance, String s) {
        if (ParallelProcessor.shouldThreadChunks())
            return;
        else instance.incrementCounter("getChunkCacheMiss");
    }

    @Inject(method = "getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/Chunk;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager$MainThreadExecutor;runTasks(Ljava/util/function/BooleanSupplier;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void callCompletableFutureHook(int x, int z, ChunkStatus leastStatus, boolean create, CallbackInfoReturnable<ChunkAccess> cir, ProfilerFiller profiler, long chunkPos, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> i) {
        DebugHookTerminator.chunkLoadDrive(this.mainThreadExecutor, i::isDone, (ServerChunkCache) (Object) this, i, chunkPos);

    }

}