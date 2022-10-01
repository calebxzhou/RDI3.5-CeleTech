package calebzhou.rdi.core.server.mixin.mt;

import net.himeki.mcmtfabric.ParallelProcessor;
import net.minecraft.Util;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Util.class)
public abstract class UtilMixin {
    /*@Inject(method = "method_28123", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/ForkJoinWorkerThread;setName(Ljava/lang/String;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void registerThread(String string, ForkJoinPool forkJoinPool, CallbackInfoReturnable<ForkJoinWorkerThread> cir, ForkJoinWorkerThread forkJoinWorkerThread) {
        ParallelProcessor.regThread(string, forkJoinWorkerThread);
    }*/
}
