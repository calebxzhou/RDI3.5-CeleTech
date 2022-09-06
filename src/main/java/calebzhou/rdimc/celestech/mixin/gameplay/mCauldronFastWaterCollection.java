package calebzhou.rdimc.celestech.mixin.gameplay;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CauldronBlock.class)
public class mCauldronFastWaterCollection {
    @Overwrite
    public static boolean shouldHandlePrecipitation(Level level, Biome.Precipitation precipitation) {
        return precipitation == Biome.Precipitation.RAIN || precipitation == Biome.Precipitation.SNOW;
    }
}
