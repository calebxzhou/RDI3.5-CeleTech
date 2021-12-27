package calebzhou.rdimc.celestech.model;

import net.minecraft.util.Identifier;

public class PlayerLocation {
    private double posX;
    private double posY;
    private double posZ;
    private float yaw;
    private float pitch;
    private String dimension;

    public PlayerLocation(double posX, double posY, double posZ, float yaw, float pitch, String dimension) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.dimension = dimension;
    }

    public Identifier getIdentifier(){
        return new Identifier(dimension);
    }

    //getset---
    public double getPosX() {
        return posX;
    }

    public PlayerLocation setPosX(double posX) {
        this.posX = posX;
        return this;
    }

    public double getPosY() {
        return posY;
    }

    public PlayerLocation setPosY(double posY) {
        this.posY = posY;
        return this;
    }

    public double getPosZ() {
        return posZ;
    }

    public PlayerLocation setPosZ(double posZ) {
        this.posZ = posZ;
        return this;
    }

    public float getYaw() {
        return yaw;
    }

    public PlayerLocation setYaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    public float getPitch() {
        return pitch;
    }

    public PlayerLocation setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public String getDimension() {
        return dimension;
    }

    public PlayerLocation setDimension(String dimension) {
        this.dimension = dimension;
        return this;
    }

}
