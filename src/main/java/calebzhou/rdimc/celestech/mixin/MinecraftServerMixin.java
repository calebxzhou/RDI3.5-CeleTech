package calebzhou.rdimc.celestech.mixin;

import net.minecraft.SharedConstants;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerMetadata;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.UserCache;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiling.jfr.FlightProfiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.function.BooleanSupplier;

import static calebzhou.rdimc.celestech.RDICeleTech.LOGGER;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
/*    @Shadow
    private long timeReference;
    @Shadow
    private long lastTimeReference;
    @Shadow
    private boolean running;
    @Shadow
    private boolean stopped;
    @Final
    @Shadow
    private ServerMetadata metadata;
    @Shadow
    private Profiler profiler;
    @Shadow
    private boolean waitingForNextTick;
    @Shadow
    private long nextTickTimestamp;
    @Shadow
    private volatile boolean loading;
    @Shadow
    private float tickTime;
    @Final
    @Shadow
    private UserCache userCache;*/

    @Redirect(
            method = "Lnet/minecraft/server/MinecraftServer;runServer()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;tick(Ljava/util/function/BooleanSupplier;)V"
            )
    )

    private void ticks(MinecraftServer instance, BooleanSupplier shouldKeepTicking){
        try{
            ((MinecraftServer)(Object)this).tick(shouldKeepTicking);
        }catch (Throwable e){
            LOGGER.error(e.getMessage());
        }
    }
//干掉崩溃
    private ServerWorld world;
    @Inject(
            method = "Lnet/minecraft/server/MinecraftServer;tickWorlds(Ljava/util/function/BooleanSupplier;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )

    private void tickworldInj(BooleanSupplier shouldKeepTicking, CallbackInfo ci, Iterator var2,ServerWorld serverWorld){
        this.world=serverWorld;
    }
    @Redirect(
            method = "Lnet/minecraft/server/MinecraftServer;tickWorlds(Ljava/util/function/BooleanSupplier;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V"
            )
    )

    private void tickworld(ServerWorld instance, BooleanSupplier shouldKeepTicking){
        try{
            this.world.tick(shouldKeepTicking);
        }catch (Throwable e){
            LOGGER.error(e.getMessage());
        }
    }
}
