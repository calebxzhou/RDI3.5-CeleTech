package calebzhou.rdi.core.server.misc

import calebzhou.rdi.core.server.logger
import net.minecraft.core.BlockPos
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.FluidTags
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.util.random.WeightedRandomList
import net.minecraft.world.entity.*
import net.minecraft.world.level.*
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.structure.BuiltinStructures
import net.minecraft.world.level.levelgen.structure.structures.NetherFortressStructure
import net.minecraft.world.phys.Vec3
import java.util.*

/**
 * Created  on 2022-10-21,21:59.
 */
object RdiMobSpawner {
  /*  fun spawnCategoryForPosition(
        category: MobCategory,
        level: ServerLevel,
        chunk: ChunkAccess,
        pos: BlockPos,
        filter: NaturalSpawner.SpawnPredicate,
        callback: NaturalSpawner.AfterSpawnCallback
    ) {
        val structureManager = level.structureManager()
        val chunkGenerator = level.chunkSource.generator
        val y = pos.y
        val blockState = chunk.getBlockState(pos)
        if (!blockState.isRedstoneConductor(chunk, pos)) {
            val mutableBlockPos = MutableBlockPos()
            var j = 0
            for (k in 0..2) {
                var x = pos.x
                var z = pos.z
                val n = 6
                var spawnerData: SpawnerData? = null
                var spawnGroupData: SpawnGroupData? = null
                var o = Mth.ceil(level.random.nextFloat() * 4.0f)
                var p = 0
                for (q in 0 until o) {
                    x += level.random.nextInt(6) - level.random.nextInt(6)
                    z += level.random.nextInt(6) - level.random.nextInt(6)
                    mutableBlockPos[x, y] = z
                    val d = x.toDouble() + 0.5
                    val e = z.toDouble() + 0.5
                    val player = level.getNearestPlayer(d, y.toDouble(), e, -1.0, false)
                    if (player != null) {
                        val f = player.distanceToSqr(d, y.toDouble(), e)
                        if (isRightDistanceToPlayerAndSpawnPoint(level, chunk, mutableBlockPos, f)) {
                            if (spawnerData == null) {
                                val optional = getRandomSpawnMobAt(
                                    level,
                                    structureManager,
                                    chunkGenerator,
                                    category,
                                    level.random,
                                    mutableBlockPos
                                )
                                if (optional.isEmpty) {
                                    break
                                }
                                spawnerData = optional.get()
                                o =
                                    spawnerData.minCount + level.random.nextInt(1 + spawnerData.maxCount - spawnerData.minCount)
                            }
                            if (isValidSpawnPostitionForType(
                                    level,
                                    category,
                                    structureManager,
                                    chunkGenerator,
                                    spawnerData,
                                    mutableBlockPos,
                                    f
                                )
                                && filter.test(spawnerData.type, mutableBlockPos, chunk)
                            ) {
                                val mob = getMobForSpawn(level, spawnerData.type) ?: return
                                mob.moveTo(d, y.toDouble(), e, level.random.nextFloat() * 360.0f, 0.0f)
                                if (isValidPositionForMob(level, mob, f)) {
                                    spawnGroupData = mob.finalizeSpawn(
                                        level,
                                        level.getCurrentDifficultyAt(mob.blockPosition()),
                                        MobSpawnType.NATURAL,
                                        spawnGroupData,
                                        null
                                    )
                                    ++j
                                    ++p
                                    level.addFreshEntityWithPassengers(mob)
                                    callback.run(mob, chunk)
                                    if (j >= mob.maxSpawnClusterSize) {
                                        return
                                    }
                                    if (mob.isMaxGroupSizeReached(p)) {
                                        break
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }*/
    private fun isRightDistanceToPlayerAndSpawnPoint(
        level: ServerLevel,
        chunk: ChunkAccess,
        pos: MutableBlockPos,
        distance: Double
    ): Boolean {
        return if (distance <= 576.0) {
            false
        } else if (level.sharedSpawnPos.closerToCenterThan(
                Vec3(
                    pos.x.toDouble() + 0.5,
                    pos.y.toDouble(),
                    pos.z.toDouble() + 0.5
                ), 24.0
            )
        ) {
            false
        } else {
            ChunkPos(pos) == chunk.pos || level.isNaturalSpawningAllowed(pos)
        }
    }
    private fun getRandomSpawnMobAt(
        level: ServerLevel,
        structureManager: StructureManager,
        generator: ChunkGenerator,
        category: MobCategory,
        random: RandomSource,
        pos: BlockPos
    ): SpawnerData? {
        val holder = level.getBiome(pos)
        return if (category == MobCategory.WATER_AMBIENT && holder.`is`(BiomeTags.REDUCED_WATER_AMBIENT_SPAWNS) && random.nextFloat() < 0.98f)
            null
        else mobsAt(
            level,
            structureManager,
            generator,
            category,
            pos,
            holder
        ).getRandom(random).get()
    }

    private fun getMobForSpawn(level: ServerLevel, entityType: EntityType<*>): Mob? {
        return try {
            val entity = entityType.create(level)
            if (entity !is Mob) {
                throw IllegalStateException("Trying to spawn a non-mob: " + Registry.ENTITY_TYPE.getKey(entityType))
            } else {
                entity
            }
        } catch (var4: Exception) {
            logger.warn("无法生成怪物 {}", var4)
            null
        }
    }
    private fun isValidPositionForMob(level: ServerLevel, mob: Mob, distance: Double): Boolean {
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
        generator: ChunkGenerator,
        data: SpawnerData,
        pos: MutableBlockPos,
        distance: Double
    ): Boolean {
        val entityType = data.type
        return if (entityType.category == MobCategory.MISC) {
            false
        } else if (!entityType.canSpawnFarFromPlayer()
            && distance > (entityType.category.despawnDistance * entityType.category.despawnDistance).toDouble()
        ) {
            false
        } else if (entityType.canSummon() && canSpawnMobAt(
                level,
                structureManager,
                generator,
                category,
                data,
                pos
            )
        ) {
            val type = SpawnPlacements.getPlacementType(entityType)
            if (!isSpawnPositionOk(type, level, pos, entityType)) {
                false
            } else if (!SpawnPlacements.checkSpawnRules(entityType, level, MobSpawnType.NATURAL, pos, level.random)) {
                false
            } else {
                level.noCollision(entityType.getAABB(pos.x.toDouble() + 0.5, pos.y.toDouble(), pos.z.toDouble() + 0.5))
            }
        } else {
            false
        }
    }
    private fun canSpawnMobAt(
        level: ServerLevel,
        structureManager: StructureManager,
        generator: ChunkGenerator,
        category: MobCategory,
        data: SpawnerData,
        pos: BlockPos
    ): Boolean {
        return mobsAt(level, structureManager, generator, category, pos, null).unwrap().contains(data)
    }

    private fun mobsAt(
        level: ServerLevel,
        structureManager: StructureManager,
        generator: ChunkGenerator,
        category: MobCategory,
        pos: BlockPos,
        biome: Holder<Biome>?
    ): WeightedRandomList<SpawnerData?> {
        return if (isInNetherFortressBounds(
                pos,
                level,
                category,
                structureManager
            )
        ) NetherFortressStructure.FORTRESS_ENEMIES else generator.getMobsAt(
            biome ?: level.getBiome(pos), structureManager, category, pos
        )
    }
    fun isSpawnPositionOk(
        placeType: SpawnPlacements.Type,
        level: LevelReader,
        pos: BlockPos,
        entityType: EntityType<*>?
    ): Boolean {
        return if (placeType == SpawnPlacements.Type.NO_RESTRICTIONS) {
            true
        } else if (entityType != null && level.worldBorder.isWithinBounds(pos)) {
            val blockState = level.getBlockState(pos)
            val fluidState = level.getFluidState(pos)
            val blockPos = pos.above()
            val blockPos2 = pos.below()
            when (placeType) {
                SpawnPlacements.Type.IN_WATER -> fluidState.`is`(FluidTags.WATER) && !level.getBlockState(blockPos)
                    .isRedstoneConductor(level, blockPos)
                SpawnPlacements.Type.IN_LAVA -> fluidState.`is`(FluidTags.LAVA)
                SpawnPlacements.Type.ON_GROUND -> {
                    val blockState2 = level.getBlockState(blockPos2)
                    if (!blockState2.isValidSpawn(level, blockPos2, entityType)) {
                        false
                    } else {
                        (NaturalSpawner.isValidEmptySpawnBlock(level, pos, blockState, fluidState, entityType)
                                && NaturalSpawner.isValidEmptySpawnBlock(
                            level,
                            blockPos,
                            level.getBlockState(blockPos),
                            level.getFluidState(blockPos),
                            entityType
                        ))
                    }
                }
                else -> {
                    val blockState2 = level.getBlockState(blockPos2)
                    if (!blockState2.isValidSpawn(level, blockPos2, entityType)) {
                        false
                    } else {
                        (NaturalSpawner.isValidEmptySpawnBlock(level, pos, blockState, fluidState, entityType)
                                && NaturalSpawner.isValidEmptySpawnBlock(
                            level,
                            blockPos,
                            level.getBlockState(blockPos),
                            level.getFluidState(blockPos),
                            entityType
                        ))
                    }
                }
            }
        } else {
            false
        }
    }
    fun isInNetherFortressBounds(
        pos: BlockPos,
        level: ServerLevel,
        category: MobCategory,
        structureManager: StructureManager
    ): Boolean {
        return if (category == MobCategory.MONSTER && level.getBlockState(pos.below()).`is`(Blocks.NETHER_BRICKS)) {
            val structure = structureManager.registryAccess()
                .registryOrThrow(Registry.STRUCTURE_REGISTRY)[BuiltinStructures.FORTRESS]
            if (structure == null) false else structureManager.getStructureAt(pos, structure).isValid
        } else {
            false
        }
    }
}
