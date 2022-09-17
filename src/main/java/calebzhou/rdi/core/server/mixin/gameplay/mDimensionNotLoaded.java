package calebzhou.rdi.core.server.mixin.gameplay;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.RdiMemoryStorage;
import calebzhou.rdi.core.server.utils.IslandUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import xyz.nucleoid.fantasy.Fantasy;
import xyz.nucleoid.fantasy.RuntimeWorldHandle;

import java.util.Optional;

@Mixin(PlayerList.class)
public class mDimensionNotLoaded {
	private ServerLevel world;
    @Inject(method = "placeNewPlayer",locals = LocalCapture.CAPTURE_FAILSOFT,
            at = @At(value = "INVOKE",target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"))
    private void teleportToSpawnWhenPlayerIslandDimensionNotLoaded(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci, GameProfile gameProfile, GameProfileCache gameProfileCache, Optional optional, String string, CompoundTag compoundTag, ResourceKey resourceKey, ServerLevel serverLevel){
		RdiCoreServer.LOGGER.info("尝试加载维度"+resourceKey.toString());
		RuntimeWorldHandle world = Fantasy.get(RdiCoreServer.getServer()).getOrOpenPersistentWorld(resourceKey.location(), IslandUtils.getIslandWorldConfig());
		this.world=world.asWorld();
    }
	@Redirect(method = "placeNewPlayer",at = @At(value = "INVOKE",target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
	private ServerLevel returnIslandLevel(MinecraftServer instance){
		return world;
	}
}
