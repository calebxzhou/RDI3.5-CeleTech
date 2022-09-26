package calebzhou.rdi.core.server.model;

import calebzhou.rdi.core.server.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.io.Serializable;

/**
 * Created by calebzhou on 2022-09-23,20:54.
 */
public class RdiPlayerLocation implements Serializable {
	private ServerLevel level;
	private double x;
	private double y;
	private double z;
	private double w;
	private double p;

	public static RdiPlayerLocation create(ServerLevel level, double x, double y, double z, double w, double p){
		return new RdiPlayerLocation(level, x, y, z, w, p);
	}
	public static RdiPlayerLocation create(ServerLevel level, Vec3i vec3i){
		return new RdiPlayerLocation(level, vec3i.getX(), vec3i.getY(), vec3i.getZ(), 0, 0);
	}
	public static RdiPlayerLocation create(ServerLevel level, Vec3 vec3){
		return new RdiPlayerLocation(level, vec3.x(), vec3.y(), vec3.z(), 0, 0);
	}


	private RdiPlayerLocation(ServerLevel level, double x, double y, double z, double w, double p) {
		this.level = level;
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		this.p = p;
	}

	public static RdiPlayerLocation create(ServerPlayer player) {
		return new RdiPlayerLocation(player.getLevel(),player.getX(),player.getY(),player.getZ(),player.getYRot(),player.getXRot());
	}

    public ServerLevel getLevel() {
		return level;
	}

	public RdiPlayerLocation setLevel(ServerLevel level) {
		this.level = level;
		return this;
	}

	public double getX() {
		return x;
	}

	public RdiPlayerLocation setX(double x) {
		this.x = x;
		return this;
	}

	public double getY() {
		return y;
	}

	public RdiPlayerLocation setY(double y) {
		this.y = y;
		return this;
	}

	public double getZ() {
		return z;
	}

	public RdiPlayerLocation setZ(double z) {
		this.z = z;
		return this;
	}

	public double getW() {
		return w;
	}

	public RdiPlayerLocation setW(double w) {
		this.w = w;
		return this;
	}

	public double getP() {
		return p;
	}

	public RdiPlayerLocation setP(double p) {
		this.p = p;
		return this;
	}

	@Override
	public String toString() {
		return "RdiPlayerLocation{" +
				"level=" + WorldUtils.getDimensionName(level) +
				", x=" + x +
				", y=" + y +
				", z=" + z +
				", w=" + w +
				", p=" + p +
				'}';
	}
}
