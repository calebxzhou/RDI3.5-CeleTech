package calebzhou.rdi.core.server.mixin.gameplay;

import calebzhou.rdi.core.server.RdiMemoryStorage;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class mDimensionNotLoaded {
	private ServerLevel world;
    @Inject(method = "placeNewPlayer",
            at = @At(shift = At.Shift.AFTER,value = "INVOKE",target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"))
    private void teleportToSpawnWhenPlayerIslandDimensionNotLoaded(Connection netManager, ServerPlayer player,CallbackInfo ci){
		RdiMemoryStorage.INSTANCE.getPidBeingGoSpawn().add(player.getStringUUID());
		/*logger.info("尝试加载维度"+resourceKey.toString());
		RuntimeWorldHandle world = Fantasy.get(RdiCoreServer.getServer()).getOrOpenPersistentWorld(resourceKey.location(), IslandUtils.getIslandWorldConfig());
		this.world=world.asWorld();*/
    }
	/*@Redirect(method = "placeNewPlayer",at = @At(value = "INVOKE",target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
	private ServerLevel returnIslandLevel(MinecraftServer instance){
		return world;
	}*/
}
