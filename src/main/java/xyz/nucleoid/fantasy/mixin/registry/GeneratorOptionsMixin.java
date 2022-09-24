package xyz.nucleoid.fantasy.mixin.registry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import xyz.nucleoid.fantasy.FantasyDimensionOptions;
import xyz.nucleoid.fantasy.util.FilteredRegistry;

import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;

@Mixin(WorldGenSettings.class)
public class GeneratorOptionsMixin {
    /*@ModifyArg(method = "m_rvgxqtqs", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/MapCodec;forGetter(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;", ordinal = 3))
    private static Function<WorldGenSettings, Registry<LevelStem>> fantasy$wrapRegistry(Function<WorldGenSettings, Registry<LevelStem>> getter) {
        return (e) -> new FilteredRegistry<>(e.dimensions(), FantasyDimensionOptions.SAVE_PREDICATE);
    }*/
}
