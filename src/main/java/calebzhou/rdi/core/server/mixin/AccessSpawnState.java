package calebzhou.rdi.core.server.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Created by calebzhou on 2022-09-28,21:55.
 */

@Mixin(NaturalSpawner.SpawnState.class)
public interface AccessSpawnState {
	@Invoker
	boolean invokeCanSpawnForCategory(MobCategory category, ChunkPos pos);
	@Invoker boolean invokeCanSpawn(EntityType<?> entityType, BlockPos pos, ChunkAccess chunk);
	@Invoker void invokeAfterSpawn(Mob mob, ChunkAccess chunk) ;

		}