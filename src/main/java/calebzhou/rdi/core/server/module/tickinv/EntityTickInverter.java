package calebzhou.rdi.core.server.module.tickinv;

import calebzhou.rdi.core.server.misc.ServerLaggingStatus;
import calebzhou.rdi.core.server.utils.ServerUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class EntityTickInverter{
    public static void handleEntityException(Exception ex, Entity entity, String msg){
        ServerUtils.broadcastChatMessage("在"+entity.toString()+"tick entity错误！"+ex+"原因："+msg+ex.getCause()+"。已经强制删除！");
        ex.printStackTrace();
        if(!(ex instanceof NullPointerException) ){
            entity.discard();
            entity=null;
        }
    }

    public static void handleTick(Consumer tickConsumer, Entity entity){
		try {
			if(entity instanceof Player) {
				tickConsumer.accept(entity);
			}else if(entity instanceof AbstractVillager villager){
				handleVillagerTick(tickConsumer,villager);
			}else if(entity instanceof Enemy monster){
				handleMonsterTick(tickConsumer,monster);
			}else if(entity instanceof Animal animal){
				handleAnimalTick(tickConsumer,animal);
			}else{
				if(!ServerLaggingStatus.isServerVeryLagging())
					tickConsumer.accept(entity);
			}
		} catch (Exception e) {
			EntityTickInverter.handleEntityException(e,entity,"3");
			entity.discard();
		}


    }



	//动物一秒5动
	private static int ticksAnimalNow=0;
	private static final int tickAnimalRequired=20/5;
	private static void handleAnimalTick(Consumer tickConsumer, Animal animal) {
		if(ticksAnimalNow>=tickAnimalRequired){
			if(!ServerLaggingStatus.isServerLagging()) tickConsumer.accept(animal);
			ticksAnimalNow=0;
		}else{
			++ticksAnimalNow;
		}
	}


	//怪物只要不卡就动
	private static void handleMonsterTick(Consumer tickConsumer, Enemy monster) {
		if(!ServerLaggingStatus.isServerLagging())
			tickConsumer.accept(monster);
	}

	//村民一秒5动
	private static int ticksVillageNow=0;
	private static final int tickVillageRequired=20/5;
	private static void handleVillagerTick(Consumer tickConsumer, AbstractVillager villager) {
		if(ticksVillageNow>=tickVillageRequired){
			if(!ServerLaggingStatus.isServerLagging())
				tickConsumer.accept(villager);
			ticksVillageNow=0;
		}else{
			++ticksVillageNow;
		}
	}

	private EntityTickInverter(){}
}
/*if(ServerLaggingStatus.worseThan20Tps()){
			if(entitiesTickFinally.contains(entity.getClass()))
				isTickable=false;
			if(entity instanceof Enemy
					|| entity instanceof PathfinderMob)
				isTickable=false;
		}
        EntityBeingTick invoker = new EntityBeingTick(tickConsumer,entity);
        try {
			if(isTickable){
				tickConsumer.accept(entity);
			}
        } catch (Exception e) {
            handleEntityException(e,entity,"7");
        }*/
//tick计时
//如果服务器延迟高于BAD
        /*if(ServerLaggingStatus.worseThan20Tps()){
			boolean remove = true;
			if(entity instanceof AbstractMinecart
					|| entity instanceof Animal
					|| entity instanceof AbstractGolem
					|| entity instanceof Player
					|| entity instanceof HangingEntity
					|| entitiesClearWhiteList.contains(entity.getClass())){
				remove = false;
			} else if(entity instanceof ItemEntity ite && ite.getItem().hasTag()){
				//掉落物品有nbt的不清除
				remove=false;
			}
			else if(entity instanceof Mob mobEnt && mobEnt.isPersistenceRequired()) {
				//有持久特性的怪物不清除
				remove=false;
			}else if(Registry.ENTITY_TYPE.getKey(entity.getType()).getNamespace().equals("botania"))
				remove=false;
			if(remove){
				RdiCoreServer.LOGGER.info("即将清除：{}",entity.toString());
				entity.remove(Entity.RemovalReason.DISCARDED);
			}
        }*/
	/*//最后tick的实体种类
	static final Set<Class<? extends Entity>> entitiesTickFinally = new ObjectOpenHashSet<>();
	//实体清除白名单
	static final Set<Class<? extends Entity>> entitiesClearWhiteList = new ObjectOpenHashSet<>();
	static {
		entitiesTickFinally.add(Villager.class);

		entitiesClearWhiteList.add(ServerPlayer.class);
		entitiesClearWhiteList.add(Villager.class);
		entitiesClearWhiteList.add(ArmorStand.class);
		entitiesClearWhiteList.add(Boat.class);
	}
*/
