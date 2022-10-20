package calebzhou.rdi.core.server.utils

import calebzhou.rdi.core.server.RdiCoreServer
import calebzhou.rdi.core.server.constant.RdiSharedConstants
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.Vec3i
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.level.material.Fluids

object WorldUtils {
    val INIT_POS = BlockPos(0, 127, 0)
    fun fill(serverWorld: ServerLevel, range: BoundingBox, block: BlockState?) {
        for (blockPos in BlockPos.betweenClosed(
            range.minX(),
            range.minY(),
            range.minZ(),
            range.maxX(),
            range.maxY(),
            range.maxZ()
        )) {
            serverWorld.setBlockAndUpdate(blockPos, block)
        }
    }

    fun getLevelByDimensionName(dimKey: String): ServerLevel? {
        val resourceLocation = ResourceLocation(dimKey)
        val worldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, resourceLocation)
        return RdiCoreServer.server.getLevel(worldKey)
    }

    val nether: ServerLevel
        get() = RdiCoreServer.server.getLevel(ServerLevel.NETHER)!!

    fun isNoPlayersInLevel(playerExitingIsland: Player, level: ServerLevel): Boolean {
        return level.getPlayers { playersInLevel: ServerPlayer -> playersInLevel.stringUUID != playerExitingIsland.stringUUID }
            .isEmpty()
    }

    @JvmStatic
	fun isNoPlayersInLevel(level: ServerLevel): Boolean {
        return level.getPlayers { p: ServerPlayer? -> true }.isEmpty()
    }

    fun getIsland2ToNetherPos(islandId: Int): Vec3i {
        var netherRatioX = 40
        var netherRatioZ = 40
        //0=一象限 3=四象限
        val quadrant = islandId % 4
        when (quadrant) {
            1 -> {
                netherRatioX *= -1
            }
            2 -> {
                netherRatioX *= -1
                netherRatioZ *= -1
            }
            3 -> {
                netherRatioZ *= -1
            }
        }
        val netherTargetX = islandId * netherRatioX
        val netherTargetZ = islandId * netherRatioZ
        val netherTargetY = 96
        return BlockPos(netherTargetX, netherTargetY, netherTargetZ)
    }

    fun getIsland2IdInt(level: Level): Int {
        return getIsland2Id(level).toInt()
    }

    fun getIsland2Id(level: Level): String {
        return getDimensionName(level).replace(RdiSharedConstants.ISLAND_DIMENSION_FULL_PREFIX, "")
    }

    @JvmStatic
	fun isInIsland2(level: Level): Boolean {
        return getDimensionName(level)
            .startsWith(RdiSharedConstants.ISLAND_DIMENSION_FULL_PREFIX)
    }

    @JvmStatic
	fun getDimensionName(level: Level): String {
        return level.dimension().location().toString()
    }

    fun getDayTime(world: Level): Int {
        return (world.dayTime % 24000L).toInt()
    }

    fun isNearbyLava(world: Level, blockPos: BlockPos): Boolean {
        //范围
        val range = 5
        val maxX = blockPos.x + range
        val maxY = blockPos.y + range
        val maxZ = blockPos.z + range
        val minX = blockPos.x - range
        val minY = blockPos.y - range
        val minZ = blockPos.z - range
        for (x in minX until maxX) for (z in minZ until maxZ) for (y in minY until maxY) {
            if (world.getBlockState(BlockPos(x, y, z)).block === Blocks.LAVA) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun placeBlock(world: Level, bpos: BlockPos?, blockState: BlockState?) {
        world.setBlockAndUpdate(bpos, blockState)
    }

    @JvmStatic
	fun placeBlock(world: Level, bpos: BlockPos?, block: Block) {
        world.setBlockAndUpdate(bpos, block.defaultBlockState())
    }

    fun getSkyLight(world: Level, bpos: BlockPos?): Int {
        return world.lightEngine.getLayerListener(LightLayer.SKY).getLightValue(bpos)
    }

    fun isInWater(world: Level, blockPos: BlockPos?): Boolean {
        return world.getFluidState(blockPos).type === Fluids.WATER ||
                world.getFluidState(blockPos).type === Fluids.FLOWING_WATER
    }

    fun getNearestPlayer(world: LevelAccessor, pos: BlockPos): Player? {
        return world.getNearestPlayer(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 20.0, false)
    }

    fun isOverworld(toLevel: ServerLevel): Boolean {
        return toLevel === RdiCoreServer.server.overworld()
    }
}
