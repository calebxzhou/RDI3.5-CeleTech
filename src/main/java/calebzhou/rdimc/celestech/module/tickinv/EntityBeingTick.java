package calebzhou.rdimc.celestech.module.tickinv;

import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

public class EntityBeingTick {
    public Consumer tickConsumer;
    public Entity entity;
    public String uuid;

    public EntityBeingTick(Consumer tickConsumer, Entity entity) {
        this.tickConsumer = tickConsumer;
        this.entity = entity;
        this.uuid=entity.getStringUUID();
    }
}
