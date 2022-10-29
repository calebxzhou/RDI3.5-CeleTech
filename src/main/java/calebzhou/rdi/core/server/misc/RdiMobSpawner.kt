package calebzhou.rdi.core.server.misc

import calebzhou.rdi.core.server.logger
import calebzhou.rdi.core.server.misc.ServerLaggingStatus.isServerLagging
import calebzhou.rdi.core.server.misc.ServerLaggingStatus.isServerVeryLagging
import calebzhou.rdi.core.server.mixin.AccessNaturalSpawner
import calebzhou.rdi.core.server.mixin.AccessSpawnPlacementData
import calebzhou.rdi.core.server.mixin.AccessSpawnPlacements
import calebzhou.rdi.core.server.utils.KRandomSource
import calebzhou.rdi.core.server.utils.ThreadPool
import net.minecraft.core.BlockPos
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.BlockTags
import net.minecraft.tags.FluidTags
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.util.random.WeightedRandomList
import net.minecraft.world.entity.*
import net.minecraft.world.level.*
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.chunk.LevelChunk
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.structure.BuiltinStructures
import net.minecraft.world.level.levelgen.structure.Structure
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride
import net.minecraft.world.level.levelgen.structure.StructureStart
import net.minecraft.world.level.levelgen.structure.structures.NetherFortressStructure
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.phys.Vec3
import java.util.function.Predicate
import kotlin.random.Random

/**
 * Created  on 2022-10-21,21:59.
 * 服务端刷怪，
 */
object RdiMobSpawner {
    @JvmStatic
    private val spawningThreadPool = ThreadPool.newSingleThreadPool("MobSpawningThread")

    @JvmStatic
    fun tick(
        level: ServerLevel,
        chunk: LevelChunk,
        forcedDespawn: Boolean
    ) {
        spawningThreadPool.submit { spawnForChunk(level, chunk, forcedDespawn) }
    }

    fun spawnForChunk(
        level: ServerLevel,
        chunk: LevelChunk,
        forcedDespawn: Boolean
    ) {
        if (isServerVeryLagging)
            return
        AccessNaturalSpawner.getSPAWNING_CATEGORIES().forEach { mobCategory ->
            if ((forcedDespawn || !mobCategory.isPersistent))
                spawnCategoryForChunk(
                    mobCategory,
                    level,
                    chunk,
                    { entityType, blockPos, chunkAccess -> true },
                    { mob, chunkAccess -> })

        }

    }

    private fun getRandomPosWithin(level: Level, chunk: LevelChunk): BlockPos {
        val chunkPos = chunk.pos
        val randX = chunkPos.minBlockX + Random.nextInt(16)
        val randZ = chunkPos.minBlockZ + Random.nextInt(16)
        val maxY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, randX, randZ) + 1
        val randY = Random.nextInt(
            level.minBuildHeight,
            maxY + 1
        )//Mth.randomBetweenInclusive(Random, level.minBuildHeight, maxY)
        return BlockPos(randX, randY, randZ)
    }

    fun spawnCategoryForChunk(
        category: MobCategory,
        level: ServerLevel,
        chunk: LevelChunk,
        filter: NaturalSpawner.SpawnPredicate,
        callback: NaturalSpawner.AfterSpawnCallback
    ) {
        level.profiler.push("spawner")
        val randomPosInChunk = getRandomPosWithin(level, chunk)
        //必须在最低高度以上才会刷怪
        if (randomPosInChunk.y >= level.minBuildHeight + 1) {
            spawnCategoryForPosition(category, level, chunk, randomPosInChunk, filter, callback)
        }
        level.profiler.pop()
    }

    @JvmStatic
    fun spawnCategoryForPosition(
        category: MobCategory,
        level: ServerLevel,
        chunk: ChunkAccess,
        pos: BlockPos,
        beforeSpawnPredicate: NaturalSpawner.SpawnPredicate,
        afterSpawnCallback: NaturalSpawner.AfterSpawnCallback
    ) {
        val blockState = chunk.getBlockState(pos)
        if (blockState.isRedstoneConductor(chunk, pos))
            return
        val structureManager = level.structureManager()
        val posY = pos.y
        var posX = pos.x
        var posZ = pos.z
        val mutableBlockPos = MutableBlockPos()
        var spawnCluster = 0
        val n = 6
        var spawnerData: SpawnerData? = null
        var spawnGroupData: SpawnGroupData? = null
        var o = Mth.ceil(Random.nextFloat() * 4.0f)
        var groupSize = 0
        for (q in 0 until o) {
            posX += Random.nextInt(n) - Random.nextInt(n)
            posZ += Random.nextInt(n) - Random.nextInt(n)
            mutableBlockPos[posX, posY] = posZ
            val posX05 = posX.toDouble() + 0.5
            val posZ05 = posZ.toDouble() + 0.5
            val player = level.getNearestPlayer(posX05, posY.toDouble(), posZ05, -1.0, false) ?: continue
            val f = player.distanceToSqr(posX05, posY.toDouble(), posZ05)
            if (!isRightDistanceToPlayerAndSpawnPoint(level, chunk, mutableBlockPos, f))
                continue
            if (spawnerData == null) {
                spawnerData =
                    getRandomSpawnMobAt(level, structureManager, category, KRandomSource, mutableBlockPos) ?: break
                o = spawnerData.minCount + Random.nextInt(1 + spawnerData.maxCount - spawnerData.minCount)
            }
            if (isValidSpawnPostitionForType(level, category, structureManager, spawnerData, mutableBlockPos, f)
                && beforeSpawnPredicate.test(spawnerData.type, mutableBlockPos, chunk)
            ) {
                val mob = getMobForSpawn(level, spawnerData.type) ?: return
                mob.moveTo(posX05, posY.toDouble(), posZ05, Random.nextFloat() * 360.0f, 0.0f)
                if (!isValidPositionForMob(level, mob, f))
                    continue
                spawnGroupData = mob.finalizeSpawn(
                    level,
                    level.getCurrentDifficultyAt(mob.blockPosition()),
                    MobSpawnType.NATURAL,
                    spawnGroupData,
                    null
                )
                ++spawnCluster
                ++groupSize
                level.addFreshEntityWithPassengers(mob)
                afterSpawnCallback.run(mob, chunk)
                if (spawnCluster >= mob.maxSpawnClusterSize)
                    return
                if (mob.isMaxGroupSizeReached(groupSize))
                    break
            }
        }
    }

    private fun isRightDistanceToPlayerAndSpawnPoint(
        level: ServerLevel,
        chunk: ChunkAccess,
        pos: MutableBlockPos,
        distance: Double
    ): Boolean {
        return if (distance <= 576.0)
            false
        else if (level.sharedSpawnPos.closerToCenterThan(
                Vec3(
                    pos.x.toDouble() + 0.5,
                    pos.y.toDouble(),
                    pos.z.toDouble() + 0.5
                ), 24.0
            )
        )
            false
        else
            ChunkPos(pos) == chunk.pos || level.isNaturalSpawningAllowed(pos)

    }

    private fun getRandomSpawnMobAt(
        level: Level,
        structureManager: StructureManager,
        category: MobCategory,
        random: RandomSource,
        pos: BlockPos
    ): SpawnerData? {
        val holder = level.getBiome(pos)
        return if (category == MobCategory.WATER_AMBIENT && holder.`is`(BiomeTags.REDUCED_WATER_AMBIENT_SPAWNS) && random.nextFloat() < 0.98f)
            null
        else mobsAt(level, structureManager, category, pos, holder).getRandom(random).orElseGet { null }
    }

    private fun getMobForSpawn(level: Level, entityType: EntityType<*>): Mob? {
        return try {
            val entity = entityType.create(level)
            if (entity !is Mob) {
                logger.warn("警告：要生成的{}不是生物", Registry.ENTITY_TYPE.getKey(entityType))
                null
            } else {
                entity
            }
        } catch (var4: Exception) {
            logger.warn("无法生成怪物 {}", var4)
            null
        }
    }

    private fun isValidPositionForMob(level: Level, mob: Mob, distance: Double): Boolean {
        return if (distance > (mob.type.category.despawnDistance * mob.type.category.despawnDistance).toDouble()
            && mob.removeWhenFarAway(distance)
        ) {
            false
        } else {
            mob.checkSpawnRules(level, MobSpawnType.NATURAL) && mob.checkSpawnObstruction(level)
        }
    }

    private fun isValidSpawnPostitionForType(
        level: ServerLevel,
        category: MobCategory,
        structureManager: StructureManager,
        data: SpawnerData,
        pos: MutableBlockPos,
        distance: Double
    ): Boolean {
        val entityType = data.type
        return if (entityType.category == MobCategory.MISC)
            false
        else if (!entityType.canSpawnFarFromPlayer()
            && distance > (entityType.category.despawnDistance * entityType.category.despawnDistance).toDouble()
        )
            false
        else if (entityType.canSummon() && canSpawnMobAt(level, structureManager, category, data, pos)) {
            val type = SpawnPlacements.getPlacementType(entityType)
            if (!isSpawnPositionOk(type, level, pos, entityType))
                false
            else if (!checkSpawnRules(entityType, level, MobSpawnType.NATURAL, pos))
                false
            else
                level.noCollision(entityType.getAABB(pos.x.toDouble() + 0.5, pos.y.toDouble(), pos.z.toDouble() + 0.5))

        } else
            false

    }

    fun <T : Entity?> checkSpawnRules(
        entityType: EntityType<T>,
        serverLevel: ServerLevelAccessor,
        spawnType: MobSpawnType,
        pos: BlockPos,
    ): Boolean {
        val data = AccessSpawnPlacements.getDATA_BY_TYPE()[entityType]
        return data == null || (data as AccessSpawnPlacementData).predicate.test(
            entityType,
            serverLevel,
            spawnType,
            pos,
            KRandomSource
        )
    }

    private fun canSpawnMobAt(
        level: Level,
        structureManager: StructureManager,
        category: MobCategory,
        data: SpawnerData,
        pos: BlockPos
    ): Boolean {
        return mobsAt(level, structureManager, category, pos, null).unwrap().contains(data)
    }

    private fun mobsAt(
        level: Level,
        structureManager: StructureManager,
        category: MobCategory,
        pos: BlockPos,
        biome: Holder<Biome>?
    ): WeightedRandomList<SpawnerData> {
        return if (isInNetherFortressBounds(pos, level, category, structureManager))
            NetherFortressStructure.FORTRESS_ENEMIES
        else
            getMobsAt(biome ?: level.getBiome(pos), structureManager, category, pos)
    }

    fun getMobsAt(
        biome: Holder<Biome>,
        structureManager: StructureManager,
        category: MobCategory,
        pos: BlockPos
    ): WeightedRandomList<SpawnerData> {
        val map = structureManager.getAllStructuresAt(pos)
        map.forEach { entry ->
            val structure = entry.key as Structure
            //找不到结构spawn override就进行下一循环
            val structureSpawnOverride = structure.spawnOverrides()[category] ?: return@forEach
            var spawnOverride = false
            val isInStructure: Predicate<StructureStart> =
                if (structureSpawnOverride.boundingBox() == StructureSpawnOverride.BoundingBoxType.PIECE)
                    Predicate { structureStart -> structureManager.structureHasPieceAt(pos, structureStart) }
                else
                    Predicate { structureStart -> structureStart.boundingBox.isInside(pos) }
            structureManager.fillStartsForStructure(structure, entry.value) { structureStart ->
                if (!spawnOverride && isInStructure.test(structureStart)) {
                    spawnOverride = true
                }
            }
            if (spawnOverride) {
                return structureSpawnOverride.spawns()
            }
        }
        return biome.value().mobSettings.getMobs(category)
    }

    fun isSpawnPositionOk(
        placeType: SpawnPlacements.Type,
        level: LevelReader,
        spawningPos: BlockPos,
        entityType: EntityType<*>?
    ): Boolean {
        return if (entityType == null)
            false
        else if (!level.worldBorder.isWithinBounds(spawningPos))
            false
        else if (placeType == SpawnPlacements.Type.NO_RESTRICTIONS) {
            true
        } else {
            val spawningBlockState = level.getBlockState(spawningPos)
            val spawningFluidState = level.getFluidState(spawningPos)
            val aboveBlockPos = spawningPos.above()
            val belowBlockPos = spawningPos.below()
            when (placeType) {
                SpawnPlacements.Type.IN_WATER -> spawningFluidState.`is`(FluidTags.WATER) && !level.getBlockState(
                    aboveBlockPos
                ).isRedstoneConductor(level, aboveBlockPos)
                SpawnPlacements.Type.IN_LAVA -> spawningFluidState.`is`(FluidTags.LAVA)
                SpawnPlacements.Type.ON_GROUND -> {
                    val belowBlockState = level.getBlockState(belowBlockPos)
                    if (!belowBlockState.isValidSpawn(level, belowBlockPos, entityType)) {
                        false
                    } else {
                        //必须是 本方块+上方方块 同时满足生成条件
                        (isValidEmptySpawnBlock(level, spawningPos, spawningBlockState, spawningFluidState, entityType)
                                && isValidEmptySpawnBlock(
                            level,
                            aboveBlockPos,
                            level.getBlockState(aboveBlockPos),
                            level.getFluidState(aboveBlockPos),
                            entityType
                        ))
                    }
                }
                else -> {
                    val belowBlockState = level.getBlockState(belowBlockPos)
                    if (!belowBlockState.isValidSpawn(level, belowBlockPos, entityType)) {
                        false
                    } else {
                        //必须是 本方块+上方方块 同时满足生成条件
                        (isValidEmptySpawnBlock(level, spawningPos, spawningBlockState, spawningFluidState, entityType)
                                && isValidEmptySpawnBlock(
                            level,
                            aboveBlockPos,
                            level.getBlockState(aboveBlockPos),
                            level.getFluidState(aboveBlockPos),
                            entityType
                        ))
                    }
                }
            }
        }
    }

    private fun isInNetherFortressBounds(
        pos: BlockPos,
        level: Level,
        category: MobCategory,
        structureManager: StructureManager
    ): Boolean {
        return if (category != MobCategory.MONSTER) false
        else if (level.getBlockState(pos.below()) == Blocks.NETHER_BRICKS) {
            structureManager.getStructureAt(
                pos,
                structureManager
                    .registryAccess()
                    .registryOrThrow(Registry.STRUCTURE_REGISTRY)[BuiltinStructures.FORTRESS]
                    ?: return false
            )
                .isValid
        } else {
            false
        }
    }

    private fun isValidEmptySpawnBlock(
        block: BlockGetter,
        pos: BlockPos,
        blockState: BlockState,
        fluidState: FluidState,
        entityType: EntityType<*>
    ): Boolean {
        //不允许生成在有碰撞箱的
        return if (blockState.isCollisionShapeFullBlock(block, pos)) {
            false
        } else if (blockState.isSignalSource) {
            //不允许生成在红石信号源
            false
        } else if (!fluidState.isEmpty) {
            //不允许生成在液体
            false//有prevent_mob_spawning_inside标签
        } else
            !blockState.`is`(BlockTags.PREVENT_MOB_SPAWNING_INSIDE)
        //不检查方块是否伤害实体 !entityType.isBlockDangerous(blockState)
    }
}
