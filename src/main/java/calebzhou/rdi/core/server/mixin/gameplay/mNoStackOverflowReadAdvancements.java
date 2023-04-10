package calebzhou.rdi.core.server.mixin.gameplay;


import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;
import java.util.Set;

@Mixin(PlayerAdvancements.class)
public abstract class mNoStackOverflowReadAdvancements {
	@Shadow
	protected abstract void load(ServerAdvancementManager manager);

	@Redirect(method = "<init>",at=@At(value = "INVOKE",target = "Lnet/minecraft/server/PlayerAdvancements;load(Lnet/minecraft/server/ServerAdvancementManager;)V"))
	private void loadNoOverflow(PlayerAdvancements instance, ServerAdvancementManager manager){
		try {
			load(manager);
		} catch (Exception e) {
			System.err.println("成就系统错误:"+e.getMessage());
		}
	}
	@Redirect(method = "reload",at=@At(value = "INVOKE",target = "Lnet/minecraft/server/PlayerAdvancements;load(Lnet/minecraft/server/ServerAdvancementManager;)V"))
	private void loadNoOverflow2(PlayerAdvancements instance, ServerAdvancementManager manager){
		try {
			load(manager);
		} catch (Exception e) {
			System.err.println("成就系统错误:"+e.getMessage());
		}
	}

	@Shadow
	@Final
	private Map<Advancement, AdvancementProgress> advancements;

	@Shadow
	protected abstract boolean hasCompletedChildrenOrSelf(Advancement advancement);

	@Shadow
	@Final
	private Set<Advancement> visible;

	@Shadow
	@Final
	private Set<Advancement> visibilityChanged;

	@Shadow
	@Final
	private Set<Advancement> progressChanged;

	@Overwrite
	private void startProgress(Advancement advancement, AdvancementProgress progress) {
		try {
			progress.update(advancement.getCriteria(), advancement.getRequirements());
			advancements.put(advancement, progress);
		} catch (Exception e) {
			System.err.println("成就系统错误:"+e.getMessage());
			e.printStackTrace();
		}
	}
	@Overwrite
	public AdvancementProgress getOrStartProgress(Advancement advancement) {
		try {
			AdvancementProgress advancementProgress = this.advancements.get(advancement);
			if (advancementProgress == null) {
				advancementProgress = new AdvancementProgress();
				this.startProgress(advancement, advancementProgress);
			}

			return advancementProgress;
		} catch (Exception e) {
			System.err.println("成就系统错误:"+e.getMessage());
			e.printStackTrace();
			return new AdvancementProgress();
		}
	}
	@Overwrite
	private boolean shouldBeVisible(Advancement advancement) {
		try {
			for(int i = 0; advancement != null && i <= 2; ++i) {
				if (i == 0 && hasCompletedChildrenOrSelf(advancement)) {
					return true;
				}

				if (advancement.getDisplay() == null) {
					return false;
				}

				AdvancementProgress advancementProgress = this.getOrStartProgress(advancement);
				if (advancementProgress.isDone()) {
					return true;
				}

				if (advancement.getDisplay().isHidden()) {
					return false;
				}

				advancement = advancement.getParent();
			}

			return false;
		} catch (Exception e) {
			System.err.println("成就系统错误:"+e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	@Overwrite
	private void ensureVisibility(Advancement advancement) {
		try {
			boolean bl = this.shouldBeVisible(advancement);
			boolean bl2 = visible.contains(advancement);
			if (bl && !bl2) {
				this.visible.add(advancement);
				this.visibilityChanged.add(advancement);
				if (this.advancements.containsKey(advancement)) {
					this.progressChanged.add(advancement);
				}
			} else if (!bl && bl2) {
				this.visible.remove(advancement);
				this.visibilityChanged.add(advancement);
			}

			if (bl != bl2 && advancement.getParent() != null) {
				this.ensureVisibility(advancement.getParent());
			}

			for(Advancement advancement2 : advancement.getChildren()) {
				this.ensureVisibility(advancement2);
			}
		} catch (Exception e) {
			System.err.println("成就系统错误:"+e.getMessage());
			e.printStackTrace();
		}

	}

}
