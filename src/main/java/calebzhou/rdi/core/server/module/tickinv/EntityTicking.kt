package calebzhou.rdi.core.server.module.tickinv

import calebzhou.rdi.core.server.RdiCoreServer
import calebzhou.rdi.core.server.logger
import calebzhou.rdi.core.server.utils.ServerUtils.broadcastChatMessage
import calebzhou.rdi.core.server.utils.ThreadPool
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.minecraft.core.Registry
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.animal.WaterAnimal
import net.minecraft.world.entity.npc.Npc
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.entity.EntityTickList
import java.util.LinkedList
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.function.Consumer

/**
 * Created  on 2022-10-26,11:18.
 */
object EntityTicking {

    @JvmStatic
    fun <T:Entity> tick(consumerEntity: Consumer<T>, entity:T){
            try {
                consumerEntity.accept(entity)
            } catch (e: Exception) {
                broadcastChatMessage("在${entity}tick entity错误！${e}原因：${e.message},${e.cause}。已经强制删除！")
                e.printStackTrace()
                if (e !is NullPointerException) {
                    entity.discard()
                }
            }
    }
    private fun shouldDiscardEntity(entity: Entity): Boolean {
        return if (RdiCoreServer.server.isSpawningAnimals
            || entity !is Animal
            && entity !is WaterAnimal
        )
            !RdiCoreServer.server.areNpcsEnabled() && entity is Npc
        else true

    }
    fun tickNonPassenger(entity: Entity, profiler: ProfilerFiller, tickList: EntityTickList) {
                entity.setOldPosAndRot()
                ++entity.tickCount
                profiler.push { Registry.ENTITY_TYPE.getKey(entity.type).toString() }
                profiler.incrementCounter("tickNonPassenger")
                entity.tick()
                profiler.pop()
                for (entity2 in entity.passengers) {
                    tickPassenger(entity, entity2,profiler,tickList)
                }

    }
    private fun tickPassenger(ridingEntity: Entity, passengerEntity: Entity, profiler: ProfilerFiller, tickList: EntityTickList) {
        if (passengerEntity.isRemoved || passengerEntity.vehicle !== ridingEntity) {
            passengerEntity.stopRiding()
        } else if (passengerEntity is Player || tickList.contains(passengerEntity)) {
            passengerEntity.setOldPosAndRot()
            ++passengerEntity.tickCount
            profiler.push { Registry.ENTITY_TYPE.getKey(passengerEntity.type).toString() }
            profiler.incrementCounter("tickPassenger")
            passengerEntity.rideTick()
            profiler.pop()
            for (entity in passengerEntity.passengers) {
                tickPassenger(passengerEntity, entity,profiler,tickList)
            }
        }
    }
    @JvmStatic
    fun handleTickListForEach(
        tickList: EntityTickList,
        level: ServerLevel
    ) {
        tickList.forEach { entity->
            if(entity==null){
                return@forEach
            }
            if(entity.isRemoved)
                return@forEach
            if (this.shouldDiscardEntity(entity)) {
                entity.discard()
                return@forEach
            }
            level.profiler.push("checkDespawn")
            entity.checkDespawn()
            level.profiler.pop()
            if (!level.chunkSource.chunkMap.distanceManager.inEntityTickingRange(entity.chunkPosition().toLong()))
                return@forEach
            val entityRiding = entity.vehicle
            if (entityRiding != null) {
                if (!entityRiding.isRemoved && entityRiding.hasPassenger(entity)) {
                    return@forEach
                }
                entity.stopRiding()
            }
            level.profiler.push("tick")
            tickNonPassenger(entity,level.profiler,tickList)
            level.profiler.pop()
        }

    }

}
