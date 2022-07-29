package calebzhou.rdimc.celestech.mixin.gameplay;

import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import calebzhou.rdimc.celestech.utils.TimeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//TODO 玩家事件
@Mixin(ServerPlayer.class)
public class mPlayerEvents {
}
//成功破坏方块
@Mixin(ServerPlayerGameMode.class)
class mBreakBlock{
    @Shadow @Final protected ServerPlayer player;


    @Inject(method = "destroyBlock",at = @At("TAIL"))
    private void breakBlock(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir){

    }

}
//成功放置方块
@Mixin(ServerPlayerGameMode.class)
class mPlaceBlock {

    @Inject(at = @At("TAIL"),method = "useItemOn")
    private void place(ServerPlayer player, Level world, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> info) {
        UseOnContext context  = new UseOnContext(player, hand, hitResult);
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = player.getLevel().getBlockState(blockPos);

    }
}
//服务器连接
@Mixin(PlayerList.class)
class mServerConnection {

    @Shadow @Final protected int maxPlayers;

    //连接服务器
    @Inject(at = @At("TAIL"),method = "placeNewPlayer")
    private void connect(Connection connection, ServerPlayer player, CallbackInfo callbackInfo){
        //发送天气预报
        ThreadPool.newThread(()-> {
            TextUtils.sendChatMessage(player, HttpUtils.sendRequest("GET","api_v1_public/getWeather","ip="+player.getIpAddress()));
            TextUtils.sendChatMessage(player, TimeUtils.getTimeChineseString()+"好,"+player.getDisplayName().getString());
        });


    }

    //断开服务器
    @Inject(at = @At("HEAD"),method = "remove")
    private void disconnect(ServerPlayer player,CallbackInfo callbackInfo){
    }
}
//玩家死亡
@Mixin(ServerPlayer.class)
class mDeath{
    @Inject(method = "die",
            at = @At(
                    value = "HEAD"
            ))
    private void onDeath(DamageSource source, CallbackInfo info){


    }
}
//TODO 玩家杀怪

//玩家说话

@Mixin(ServerGamePacketListenerImpl.class)
class mPlayerChat {

    @Inject(method= "handleChat(Lnet/minecraft/network/protocol/game/ServerboundChatPacket;Lnet/minecraft/server/network/FilteredText;)V",
            at=@At(value="HEAD"))
    private void handleMessage(ServerboundChatPacket serverboundChatPacket, FilteredText<String> filteredText, CallbackInfo ci) {
    }
}