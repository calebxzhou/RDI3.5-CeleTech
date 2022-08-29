package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.RdiSharedConstants;
import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.WorldUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;

import java.util.NoSuchElementException;

public class ChangeBiomeCommand extends RdiCommand {
    public ChangeBiomeCommand() {
        super("change-biome");
    }
    private static final DynamicCommandExceptionType ERROR_BIOME_NOT_FOUND = new DynamicCommandExceptionType(object -> Component.translatable("commands.locate.biome.not_found", object));

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.then(
                Commands.argument("biome", ResourceOrTagLocationArgument.resourceOrTag(Registry.BIOME_REGISTRY))
                        .executes(context->changeBiome(
                                context.getSource().getPlayer(),
                                ResourceOrTagLocationArgument.getRegistryType(
                                        context,
                                        "biome",
                                        Registry.BIOME_REGISTRY,
                                        ERROR_BIOME_NOT_FOUND)
                        )
                        )
        );
    }

    //worldedit抄的，232行，
    // https://github.com/EngineHub/WorldEdit/blob/b4ae41a4b65876650d2538aa91847e0d49ca79cf/worldedit-fabric/src/main/java/com/sk89q/worldedit/fabric/FabricWorld.java
    final static int xpNeed = 80;
    private int changeBiome(ServerPlayer player, ResourceOrTagLocationArgument.Result<Biome> biomeType){
        if(!WorldUtils.getDimensionName(player.level).startsWith(RdiSharedConstants.MOD_ID)){
            TextUtils.sendChatMessage(player,MessageType.ERROR,"只有在二岛上才能改变生物群系！");
            return 1;
        }
        if(player.experienceLevel<xpNeed){
            TextUtils.sendChatMessage(player,MessageType.ERROR,"您经验不足"+xpNeed);
            return 1;
        }
        ServerLevel level = player.getLevel();
        ResourceKey<Biome> biomeResourceKey = null;

        Holder<Biome> biomeHolder = null;
        try {
            biomeResourceKey = biomeType.unwrap().left().get();
            biomeHolder = level.registryAccess().registry(Registry.BIOME_REGISTRY)
                    .orElseThrow()
                    .getHolderOrThrow(biomeResourceKey);

            //for string biome type: ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(biomeType))
        } catch (NoSuchElementException e) {
            TextUtils.sendChatMessage(player, MessageType.ERROR,"找不到%s这个群系！%s".formatted(biomeType,e.getMessage()));
            return 1 ;
        }

        if(biomeHolder == null){
            TextUtils.sendChatMessage(player, MessageType.ERROR,"找不到%s这个群系！".formatted(biomeType));
            return 1 ;
        }

        BlockPos position = player.blockPosition();
        ChunkAccess chunk = level.getChunk(position.getX() >> 4, position.getZ() >> 4);
        // Screw it, we know it's really mutable...
        LevelChunkSection section = chunk.getSection(chunk.getSectionIndex(position.getY()));
        var biomeArray = (PalettedContainer<Holder<Biome>>) section.getBiomes();

        biomeArray.getAndSetUnchecked(
                position.getX() & 3, position.getY() & 3, position.getZ() & 3,
                biomeHolder
        );
        chunk.setUnsaved(true);
        player.experienceLevel-=xpNeed;
        TextUtils.sendChatMessage(player,MessageType.SUCCESS,"将您附近一个区域设定成了生物群系：%s ！重新载入区块后，更改将会生效。".formatted(biomeResourceKey.location()));
        return 1;
    }
}
