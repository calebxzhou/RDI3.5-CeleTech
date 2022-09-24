package calebzhou.rdi.core.server.module.tickinv;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.ServerStatus;
import calebzhou.rdi.core.server.utils.ServerUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;

import java.util.Set;
import java.util.function.Consumer;

public class EntityTickInverter implements ITickDelayable{
    public static final EntityTickInverter INSTANCE= new EntityTickInverter();
    private final Object2ObjectLinkedOpenHashMap<String,EntityBeingTick> delayTickList = new Object2ObjectLinkedOpenHashMap<>();
    private static final int ENTITY_TICK_LIMIT = 40;//ms 实体40ms没tick完就直接冻结
    public static void handleEntityException(Exception ex, Entity entity, String msg){
        ServerUtils.broadcastChatMessage("在"+entity.toString()+"tick entity错误！"+ex+"原因："+msg+ex.getCause()+"。已经强制删除！");
        ex.printStackTrace();
        if(!(ex instanceof NullPointerException) ){
            entity.discard();
            entity=null;
        }
    }
    public int getDelayTickListSize(){
        return delayTickList.size();
    }
    public void releaseDelayTickList(){

        if(delayTickList.size()==0)
            return;
        String uid = delayTickList.firstKey();
        if(ServerStatus.flag< ServerStatus.BAD){
            EntityBeingTick entityBeingTick = delayTickList.get(uid);
            entityBeingTick.tickConsumer.accept(entityBeingTick.entity);
            delayTickList.removeFirst();
        }
    }
	//最后tick的实体种类
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
    public void tick(Consumer tickConsumer, Entity entity){
		boolean isTickable = true;
		if(ServerStatus.worseThan20Tps()){
			if(entitiesTickFinally.contains(entity.getClass()))
				isTickable=false;
			if(entity instanceof Enemy
					|| entity instanceof PathfinderMob)
				isTickable=false;
		}
        EntityBeingTick invoker = new EntityBeingTick(tickConsumer,entity);
        if(delayTickList.containsKey(entity.getStringUUID())){
            isTickable=false;
        }
        try {
			if(isTickable){
				tickConsumer.accept(entity);
			}
        } catch (Exception e) {
            handleEntityException(e,entity,"7");
        }
        //tick计时
        //如果服务器延迟高于BAD
        if(ServerStatus.worseThan20Tps()){
            delayTickList.put(entity.getStringUUID(),invoker);
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
        }

    }
    private EntityTickInverter(){}
}
