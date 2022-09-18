package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.NoSuchElementException;

import static calebzhou.rdi.core.server.utils.PlayerUtils.isInIsland;
import static calebzhou.rdi.core.server.utils.PlayerUtils.sendChatMessage;

public class ChangeBiomeCommand extends RdiCommand {
	private ChangeBiomeCommand() {
        super("change-biome","改变一个区域内的生物群系");
    }
	static {
		RdiCommand.register(new ChangeBiomeCommand());
	}
    private static final DynamicCommandExceptionType ERROR_BIOME_NOT_FOUND = new DynamicCommandExceptionType(object -> Component.translatable("commands.locate.biome.not_found", object));

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder
                .then(
                    Commands.argument("biome", ResourceOrTagLocationArgument.resourceOrTag(Registry.BIOME_REGISTRY))
                            .then(
                                    Commands.argument("pos1",BlockPosArgument.blockPos())
                                            .then(
                                                    Commands.argument("pos2",BlockPosArgument.blockPos())
                                                            .executes(this::changeBiomeWith2Pos)
                                            )
                            )
        );
    }
    //worldedit抄的，232行，
    // https://github.com/EngineHub/WorldEdit/blob/b4ae41a4b65876650d2538aa91847e0d49ca79cf/worldedit-fabric/src/main/java/com/sk89q/worldedit/fabric/FabricWorld.java
    final static int xpNeedBase = 2;

    private int changeBiomeWith2Pos(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayer();
        if(!isInIsland(player)){
            sendChatMessage(player,PlayerUtils.RESPONSE_ERROR,"只有在二岛上才能改变生物群系！");
            return 1;
        }
        BlockPos blockPos1 = BlockPosArgument.getLoadedBlockPos(context,"pos1");
        BlockPos blockPos2 = BlockPosArgument.getLoadedBlockPos(context,"pos2");
        int xpLvlNeed = (int)Math.cbrt(blockPos2.distSqr(blockPos1))*xpNeedBase;
        if(player.experienceLevel < xpLvlNeed){
            sendChatMessage(player, PlayerUtils.RESPONSE_ERROR,"您经验不足等级"+ xpLvlNeed+"，您只有等级"+player.experienceLevel);
            return 1;
        }
        var biomeType = ResourceOrTagLocationArgument.getRegistryType(context, "biome", Registry.BIOME_REGISTRY, ERROR_BIOME_NOT_FOUND);
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
            sendChatMessage(player, PlayerUtils.RESPONSE_ERROR,"找不到%s这个群系！%s".formatted(biomeType,e.getMessage()));
            return 1 ;
        }


        final Holder<Biome> finalBiomeHolder = biomeHolder;
        BoundingBox box = BoundingBox.fromCorners(blockPos1,blockPos2);
        BlockPos.betweenClosedStream(box)
                .forEach(bpos->{
                    ChunkAccess chunk = level.getChunk(bpos.getX() >> 4, bpos.getZ() >> 4);
                    // Screw it, we know it's really mutable...
                    LevelChunkSection section = chunk.getSection(chunk.getSectionIndex(bpos.getY()));
                    var biomeArray = (PalettedContainer<Holder<Biome>>) section.getBiomes();
                    biomeArray.getAndSetUnchecked(
                            bpos.getX() & 3, bpos.getY() & 3, bpos.getZ() & 3,
                            finalBiomeHolder
                    );
                    chunk.setUnsaved(true);
                });
        player.experienceLevel -= xpLvlNeed;
        sendChatMessage(player,PlayerUtils.RESPONSE_SUCCESS,"将您附近一个区域设定成了生物群系：%s ！重新载入区块后，更改将会生效。".formatted(biomeResourceKey.location()));
        return 1;
    }


}
