package calebzhou.rdimc.celestech.mixin.gameplay;

import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.constant.WorldConst;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(PlayerList.class)
public class mDimensionNotLoaded {
    @Inject(method = "placeNewPlayer",locals = LocalCapture.CAPTURE_FAILSOFT,
            at = @At(value = "INVOKE",target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"))
    private void teleportToSpawnWhenPlayerIslandDimensionNotLoaded(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci, GameProfile gameProfile, GameProfileCache gameProfileCache, Optional optional, String string, CompoundTag compoundTag, ResourceKey resourceKey, ServerLevel serverLevel){
        //TextUtils.sendChatMessage(serverPlayer, MessageType.ERROR,"存档“%s”尚未载入。如果这个是您岛屿的存档，请使用指令/home2或者按下H键载入存档。现在将您传送到主岛.....".formatted(resourceKey));
        //PlayerUtils.teleport(serverPlayer, WorldConst.SPAWN_LOCA);
    }
}
