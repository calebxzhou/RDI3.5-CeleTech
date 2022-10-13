package net.himeki.mcmtfabric.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.concurrent.Semaphore;
import net.minecraft.util.ThreadingDetector;

@Mixin(ThreadingDetector.class)
public abstract class LockHelperMixin<T> {

    @Shadow
    @Final
    @Mutable
    private Semaphore semaphore = new Semaphore(255);
}