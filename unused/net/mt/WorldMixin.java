package calebzhou.rdi.core.server.mixin.mt;

import net.himeki.mcmtfabric.ParallelProcessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Level.class)
public abstract class WorldMixin implements LevelAccessor, AutoCloseable {
   /* @Shadow
    @Final
    @Mutable
    private Thread thread;

    @Shadow
    @Final
    protected List<TickingBlockEntity> blockEntityTickers;

    @Inject(method = "tickBlockEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
    private void postEntityPreBlockEntityTick(CallbackInfo ci) {
        if ((Object) this instanceof ServerLevel) {
            ServerLevel thisWorld = (ServerLevel) (Object) this;
            ParallelProcessor.postEntityTick(thisWorld);
            ParallelProcessor.preBlockEntityTick(thisWorld);
        }
    }

    @Inject(method = "tickBlockEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V"))
    private void postBlockEntityTick(CallbackInfo ci) {
        if ((Object) this instanceof ServerLevel) {
            ServerLevel thisWorld = (ServerLevel) (Object) this;
            ParallelProcessor.postBlockEntityTick(thisWorld);
        }
    }

    @Redirect(method = "tickBlockEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/BlockEntityTickInvoker;tick()V"))
    private void overwriteBlockEntityTick(TickingBlockEntity blockEntityTickInvoker) {
        ParallelProcessor.callBlockEntityTick(blockEntityTickInvoker, (Level) (Object) this);
    }

    @Redirect(method = "getBlockEntity", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;currentThread()Ljava/lang/Thread;"))
    private Thread overwriteCurrentThread() {
        return this.thread;
    }*/
}
