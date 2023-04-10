package calebzhou.rdi.core.server.mixin.gameplay;

import calebzhou.rdi.core.server.RdiCoreServer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created  on 2023-04-09,19:15.
 */
public class mHardEntity {
}

@Mixin(Blaze.BlazeAttackGoal.class)
class mBlaze {
	//烈焰人加速攻击
	@ModifyConstant(method = "tick", constant = @Constant(intValue = 1, ordinal = 2))
	private int quickAttack(int i) {
		return 20;
	}
}

@Mixin(RangedBowAttackGoal.class)
class mBowSpeedUp {
	//弓箭加速攻击
	@ModifyConstant(method = "tick", constant = @Constant(intValue = 20, ordinal = 2))
	private int moreFrequentAttack(int constant) {
		return 2;
	}
}

@Mixin(Slime.class)
class mSlime {
	//史莱姆变大 生命值x2 变快 攻击力增强
	@Inject(method = "setSize(IZ)V",
			at = @At(value = "TAIL"))
	private void setSize(int size, boolean heal, CallbackInfo ci) {
		int size2 = Mth.clamp(size, 1, 127);
		Slime slime = ((Slime) ((Object) (this)));
		slime.getAttribute(Attributes.MAX_HEALTH).setBaseValue(size2 * size2 * 2);
		slime.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.8F + 0.1F * (float) size2);
		slime.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(size2 * 2);
		slime.setHealth(size2 * size2 * 2);
	}
}

@Mixin(Mob.class)
class mMobNoDayBurn {
	//所有怪物白天不着火，除了幻翼
	@Overwrite
	public boolean isSunBurnTick() {
		Mob mob = (Mob) (Object) this;
		if (mob.getLevel().isDay() && mob.getType() == EntityType.PHANTOM)
			return true;
		else
			return false;
	}
}

@Mixin(LlamaSpit.class)
class mLlamaSpit {
	//羊驼吐一口就恶心
	@Inject(method = "onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
	private void plusDamage(EntityHitResult entityHitResult, CallbackInfo ci) {
		if (entityHitResult.getEntity() instanceof Player player) {
			player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 15 * 20, 2));
			player.addEffect(new MobEffectInstance(MobEffects.POISON, 15 * 20, 2));
		}

	}
}

@Mixin(Zombie.class)
class mZombie extends Mob {
	protected mZombie(EntityType<? extends Mob> entityType, Level world) {
		super(entityType, world);
	}

	//僵尸 血量++ 攻速++ 伤害++ 移速++
	@Overwrite
	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.FOLLOW_RANGE, 64.0)
				.add(Attributes.MOVEMENT_SPEED, 0.40f)
				.add(Attributes.ATTACK_DAMAGE, 4.0)
				.add(Attributes.ARMOR, 2.0)
				.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE)
				.add(Attributes.MAX_HEALTH, 50);
	}

	// 僵尸拿铁剑
	@Overwrite
	public void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficulty) {
		super.populateDefaultEquipmentSlots(randomSource, difficulty);
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
	}

	// 僵尸AI增强
	@Overwrite
	public void addBehaviourGoals() {
		Zombie zombie = (Zombie) (Object) this;
		this.goalSelector.addGoal(2, new ZombieAttackGoal(zombie, 1.0, true));
		this.goalSelector.addGoal(6, new MoveThroughVillageGoal(zombie, 1.0, true, 4, zombie::canBreakDoors));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(zombie).setAlertOthers(ZombifiedPiglin.class));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
		//this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Zombie.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Witch.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, AbstractVillager.class, false));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
		this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
	}

}

@Mixin(SweetBerryBushBlock.class)
class mSweetBerry {
	//浆果丛扎人更疼
	@ModifyConstant(method = "entityInside(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)V",
			constant = @Constant(floatValue = 1.0f))
	private static float modifyDamage(float f) {
		return 5.5f;
	}
}

@Mixin(EnderDragon.class)
class mEnderDragon {
	//末影龙血量++
	@Overwrite
	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4800);
	}
}

@Mixin(Witch.class)
class mWitch {
	//女巫增强
	@Overwrite
	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 50.0)
				.add(Attributes.MOVEMENT_SPEED, 0.5);
	}

	//缩短攻击间隔
	@ModifyConstant(method = "registerGoals",
			constant = @Constant(intValue = 60))
	private int intervalMinus(int constant) {
		return 5;
	}
}

@Mixin(Phantom.class)
class mPhantom {
	//幻翼更疼
	@ModifyConstant(method = "updatePhantomSizeInfo", constant = @Constant(intValue = 6))
	private int changeDamange(int constant) {
		return 15;
	}

}
@Mixin(FireBlock.class)
class mFireSpeedUp {
	//火加速
	@Overwrite
	private static int getFireTickDelay(RandomSource randomSource){
		return 2 + RdiCoreServer.RANDOM.nextInt(10);
	}
}



@Mixin(EnderMan.class)
class mEnderman {
//末影人增强

	//拿了方块也会被despawn
	@Overwrite
	public boolean requiresCustomPersistence() {
		return false;
	}

}
@Mixin(AbstractSkeleton.class)
class mSkeleton extends Monster{
	//骷髅增强
	private mSkeleton(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@ModifyConstant(
			method = "reassessWeaponGoal()V",
			constant = @Constant(intValue = 20)
	)
	private static int changeAtkSpeed(int spd){
		return 2;
	}
	@Overwrite
	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.4)
				.add(Attributes.MAX_HEALTH,50);
	}
	@Overwrite
	public void registerGoals() {
		this.goalSelector.addGoal(1, new AvoidEntityGoal(this, Wolf.class, 6.0F, 1.0, 1.2));
		this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 32.0F));
		this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
	}
}
@Mixin(LivingEntity.class)
 class mQuickDrown {
	private static final int MAX_AIR = 300;
	//更容易溺水
	@Overwrite
	public int decreaseAirSupply(int air) {
		int i = EnchantmentHelper.getRespiration((LivingEntity)(Object) this);
		if (i > 0 && RdiCoreServer.RANDOM.nextInt(i + 1) > 0) {
			return air;
		}
		return air - 5;
	}
	//更不容易恢复氧气
	@Overwrite
	public int increaseAirSupply(int air) {
		return Math.min(air + 2, MAX_AIR);
	}
	@ModifyConstant(method = "baseTick()V",
			constant = @Constant(floatValue = 2.0f))
	private static float drownDeath(float constant){
		return 5.0f;
	}
}

//恶魂 增强
@Mixin(Ghast.class)
class mGhast {

	//50血
	@Overwrite
	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.FOLLOW_RANGE, 64.0);
	}
	//生成概率x4
	@Overwrite
	public static boolean checkGhastSpawnRules(EntityType<Ghast> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
		return randomSource.nextInt(5) == 0 && Ghast.checkMobSpawnRules(entityType, levelAccessor, mobSpawnType, blockPos, randomSource);
	}

	//一次生成4个
	@Overwrite
	public int getMaxSpawnClusterSize() {
		return 4;
	}
}
@Mixin(Ghast.GhastMoveControl.class)
class mGhastMove {
	@ModifyConstant(
			method = "tick"
			,constant = @Constant(doubleValue = 0.1D)
	)
	private static double change(double d){
		return 0.2D;
	}
}
@Mixin(Ghast.RandomFloatAroundGoal.class)
class mGhastFly {
	@ModifyConstant(
			method = "start"
			,constant = @Constant(doubleValue = 1.0D)
	)
	private static double change(double d){
		return 1.5D;
	}
}
@Mixin(Ghast.GhastShootFireballGoal.class)
class mGhastShoot{
	@Shadow
	@Mutable
	public int chargeTime;
	@Shadow @Final
	private Ghast ghast;

	@ModifyConstant(
			method = "tick"
			,constant = @Constant(intValue = 20)
	)
	private static int changeCD(int constant){
		return 11;
	}
	@ModifyConstant(
			method = "tick"
			,constant = @Constant(doubleValue = 4.0D)
	)
	private static double changeSped(double constant){
		return 5.0D;
	}

	@Inject(
			method = "tick",
			at=@At("TAIL")
	)
	private void changeCd2(CallbackInfo ci){
		if(chargeTime<=-40){
			chargeTime=9;
			this.ghast.setCharging(true);
		}
	}
}
