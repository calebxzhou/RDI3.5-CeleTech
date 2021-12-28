package calebzhou.rdimc.celestech.model;

import com.google.gson.Gson;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.Serializable;

public class PlayerHome implements Serializable {
    String playerUuid;
    String homeName;
    String dimension;

    Double posX,posY,posZ;
    Float yaw,pitch;
    String comments;

    public PlayerHome(String playerUuid, String homeName, String dimension, Double posX, Double posY, Double posZ, Float yaw, Float pitch, String comments) {
        this.playerUuid = playerUuid;
        this.homeName = homeName;
        this.dimension = dimension;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.comments = comments;
    }

    public String getPlayerUuid() {
        return playerUuid;
    }

    public String getHomeName() {
        return homeName;
    }

    public String getDimension() {
        return dimension;
    }

    public Double getPosX() {
        return posX;
    }

    public Double getPosY() {
        return posY;
    }

    public Double getPosZ() {
        return posZ;
    }

    public Float getYaw() {
        return yaw;
    }

    public Float getPitch() {
        return pitch;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
    public static PlayerHome fromPlayerLocation(ServerPlayerEntity player,String homeName){
        PlayerHome playerHome = new PlayerHome(
                player.getUuidAsString(),
                homeName,
                player.getWorld().getDimension().getEffects().toString(),
                player.getX(),
                player.getY(),
                player.getZ(),
                player.getYaw(),
                player.getPitch(),
                "空岛传送点");
        return playerHome;
    }
}
