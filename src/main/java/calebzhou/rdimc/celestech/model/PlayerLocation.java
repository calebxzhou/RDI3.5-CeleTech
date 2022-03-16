package calebzhou.rdimc.celestech.model;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class PlayerLocation {
    private double posX;
    private double posY;
    private double posZ;
    private float yaw;
    private float pitch;
    private ServerLevel world;

    public static PlayerLocation fromPlayer(Player player){
        return new PlayerLocation(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot(), (ServerLevel) player.getLevel());
    }
    public static PlayerLocation fromBlockPos(BlockPos bpos,ServerLevel dim,float yaw,float pitch){
        return new PlayerLocation(bpos.getX(), bpos.getY(), bpos.getZ(),yaw,pitch,dim);
    }

    public PlayerLocation() {
    }

    public PlayerLocation(double posX, double posY, double posZ, float yaw, float pitch, ServerLevel world) {
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

    public ServerLevel getWorld() {
        return world;
    }

    public void setWorld(ServerLevel world) {
        this.world = world;
    }
}
