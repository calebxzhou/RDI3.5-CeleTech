package calebzhou.rdi.core.server.command.impl

import calebzhou.rdi.core.server.command.RdiNormalCommand
import calebzhou.rdi.core.server.utils.PlayerUtils
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.chunk.PalettedContainer
import net.minecraft.world.level.levelgen.structure.BoundingBox

class ChangeBiomeCommand : RdiNormalCommand("change-biome", "改变一个区域内的生物群系",true) {
    override val execution : LiteralArgumentBuilder<CommandSourceStack>
    get() = baseArgBuilder
            .then(
                Commands.argument("biome", ResourceOrTagLocationArgument.resourceOrTag(Registry.BIOME_REGISTRY))
                    .then(
                        Commands.argument("pos1", BlockPosArgument.blockPos())
                            .then(
                                Commands.argument("pos2", BlockPosArgument.blockPos())
                                    .executes { context: CommandContext<CommandSourceStack> ->
                                        changeBiomeWith2Pos(
                                            context
                                        )
                                    }
                            )
                    )
            )


    //worldedit抄的，232行，
    // https://github.com/EngineHub/WorldEdit/blob/b4ae41a4b65876650d2538aa91847e0d49ca79cf/worldedit-fabric/src/main/java/com/sk89q/worldedit/fabric/FabricWorld.java
    private fun changeBiomeWith2Pos(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.player!!
        if (!PlayerUtils.isInIsland(player)) {
            PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "只有在岛上才能改变生物群系！")
            return 1
        }
        val blockPos1 = BlockPosArgument.getLoadedBlockPos(context, "pos1")
        val blockPos2 = BlockPosArgument.getLoadedBlockPos(context, "pos2")
        val xpLvlNeed = Math.cbrt(blockPos2.distSqr(blockPos1)).toInt() * xpNeedBase
        if (player.experienceLevel < xpLvlNeed) {
            PlayerUtils.sendChatMessage(
                player,
                PlayerUtils.RESPONSE_ERROR,
                "您经验不足等级" + xpLvlNeed + "，您只有等级" + player.experienceLevel
            )
            return 1
        }
        val biomeType = ResourceOrTagLocationArgument.getRegistryType(
            context,
            "biome",
            Registry.BIOME_REGISTRY,
            ERROR_BIOME_NOT_FOUND
        )
        val level = player.getLevel()
        val biomeResourceKey: ResourceKey<Biome>
        val biomeHolder: Holder<Biome>
        try {
            biomeResourceKey = biomeType.unwrap().left().get()
            biomeHolder = level.registryAccess().registry(Registry.BIOME_REGISTRY)
                .orElseThrow()
                .getHolderOrThrow(biomeResourceKey)
            //for string biome type: ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(biomeType))
        } catch (e: NoSuchElementException) {
            PlayerUtils.sendChatMessage(
                player,
                PlayerUtils.RESPONSE_ERROR,
                "找不到%s这个群系！%s".format(biomeType, e.message)
            )
            return 1
        }
        val box = BoundingBox.fromCorners(blockPos1, blockPos2)
        BlockPos.betweenClosedStream(box)
            .forEach { bpos: BlockPos ->
                val chunk: ChunkAccess = level.getChunk(bpos.x shr 4, bpos.z shr 4)
                // Screw it, we know it's really mutable...
                val section = chunk.getSection(chunk.getSectionIndex(bpos.y))
                val biomeArray = section.biomes as PalettedContainer<Holder<Biome>>
                biomeArray.getAndSetUnchecked(bpos.x and 3, bpos.y and 3, bpos.z and 3, biomeHolder)
                chunk.isUnsaved = true
            }
        player.experienceLevel -= xpLvlNeed
        PlayerUtils.sendChatMessage(
            player,
            PlayerUtils.RESPONSE_SUCCESS,
            "将您附近一个区域设定成了生物群系：%s ！重新载入区块后，更改将会生效。".format(biomeResourceKey.location())
        )
        return 1
    }

    companion object {
        private val ERROR_BIOME_NOT_FOUND = DynamicCommandExceptionType { `object`: Any? ->
            Component.translatable(
                "commands.locate.biome.not_found",
                `object`
            )
        }

        const val xpNeedBase = 2
    }
}
