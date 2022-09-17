package calebzhou.rdi.celestech.command.impl.island;

import calebzhou.rdi.celestech.RDICeleTech;
import calebzhou.rdi.celestech.command.RdiCommand;
import calebzhou.rdi.celestech.constant.MessageType;
import calebzhou.rdi.celestech.model.PlayerLocation;
import calebzhou.rdi.celestech.thread.RdiHttpRequest;
import calebzhou.rdi.celestech.utils.HttpUtils;
import calebzhou.rdi.celestech.utils.RandomUtils;
import calebzhou.rdi.celestech.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.OptionalLong;

import static calebzhou.rdi.celestech.RDICeleTech.ISLAND_DIMENSION_PREFIX;
import static calebzhou.rdi.celestech.utils.TextUtils.sendChatMessage;

public class CreateCommand extends RdiCommand {

    public CreateCommand(   ) {
        super("create");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> exec(context.getSource().getPlayer()));
    }

    private int exec(ServerPlayer player) {

    }

}
/* OptionalLong fixedTime=null;
        int coordinateScale=1;
        boolean hasSkyLight=true, hasCeiling=false, ultraWarm=false, natural=true , bedWorks=true, respawnAnchorWorks=true;
        int minY=-64 , height=384, logicalHeight=384;
        TagKey<Block> infiniburn= BlockTags.INFINIBURN_OVERWORLD;
        float ambientLight=0f;
        DimensionType.MonsterSettings monsterSetting = new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 0);
        ResourceLocation overworldEffects = BuiltinDimensionTypes.OVERWORLD_EFFECTS;

        DimensionType dimensionType = new DimensionType(OptionalLong.empty(),hasSkyLight,hasCeiling,ultraWarm,natural,coordinateScale,bedWorks,respawnAnchorWorks,minY,height,logicalHeight,infiniburn,overworldEffects,ambientLight,monsterSetting);
        ResourceKey<Level> newDimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(RDICeleTech.MODID, ISLAND_DIMENSION_PREFIX+RandomUtils.getRandomIslandId()));
        */
