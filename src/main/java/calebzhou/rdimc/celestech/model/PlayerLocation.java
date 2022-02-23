package calebzhou.rdimc.celestech.model;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class PlayerLocation {
    private double posX;
    private double posY;
    private double posZ;
    private float yaw;
    private float pitch;
    private ServerWorld world;

    public static PlayerLocation fromPlayer(PlayerEntity player){
        return new PlayerLocation(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch(), (ServerWorld) player.getWorld());
    }
    public static PlayerLocation fromBlockPos(BlockPos bpos,ServerWorld dim,float yaw,float pitch){
        return new PlayerLocation(bpos.getX(), bpos.getY(), bpos.getZ(),yaw,pitch,dim);
    }

    public PlayerLocation() {
    }

    public PlayerLocation(double posX, double posY, double posZ, float yaw, float pitch, ServerWorld world) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public ServerWorld getWorld() {
        return world;
    }

    public void setWorld(ServerWorld world) {
        this.world = world;
    }
}
