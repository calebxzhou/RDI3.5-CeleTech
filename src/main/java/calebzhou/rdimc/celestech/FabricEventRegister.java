package calebzhou.rdimc.celestech;

import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.command.impl.*;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.module.DeathRandomDrop;
import calebzhou.rdimc.celestech.utils.*;
import com.mojang.brigadier.CommandDispatcher;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.io.File;

public class FabricEventRegister {


    public FabricEventRegister(){
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
        ServerPlayConnectionEvents.JOIN.register(this::onPlayerJoin);
        ServerPlayConnectionEvents.DISCONNECT.register(this::onPlayerDisconnect);
        AttackBlockCallback.EVENT.register(this::onBreakBlock);
        ServerMessageEvents.CHAT_MESSAGE.register(this::onPlayerChat);
        ServerPlayerEvents.ALLOW_DEATH.register(this::onPlayerDeath);
    }



    //玩家聊天
    private void onPlayerChat(FilteredText<PlayerChatMessage> text, ServerPlayer player, ResourceKey<ChatType> key) {
        String cont = text.raw().signedContent().getString();
        String pid = player.getStringUUID();
        ThreadPool.newThread(()->{
            HttpUtils.sendRequest("post","record/chat","pid="+pid,"cont="+EncodingUtils.getUTF8StringFromGBKString(cont));
        });
    }

    //玩家死亡
    private boolean onPlayerDeath(ServerPlayer player, DamageSource source, float damage) {
        String pid = player.getStringUUID();
        String src = source.getMsgId();
        //记录死亡
        ThreadPool.newThread(()->{
            HttpUtils.sendRequest("post","record/death","pid="+pid,"src="+src);
        });
        //随机掉落物品
        DeathRandomDrop.handleDeath(player);
        return true;
    }

    //玩家断开服务器
    private void onPlayerDisconnect(ServerGamePacketListenerImpl listener, MinecraftServer server) {
        ServerPlayer player = listener.getPlayer();
        RDICeleTech.afkMap.removeInt(player.getScoreboardName());
        RDICeleTech.tpaMap.remove(player.getStringUUID());
    }
    //破坏方块
    private InteractionResult onBreakBlock(Player player, Level level, InteractionHand hand, BlockPos blockPos, Direction direction) {

        return InteractionResult.PASS;
    }

    //玩家连接服务器 连接成功
    private void onPlayerJoin(ServerGamePacketListenerImpl listener, PacketSender sender, MinecraftServer server) {
        ServerPlayer player = listener.getPlayer();
        ThreadPool.newThread(()-> {
            File pwdFile = PlayerUtils.getPasswordFile(player);
            if(!pwdFile.exists()){
                TextUtils.sendChatMessage(player, MessageType.INFO,"您的账号数据尚未加密，可能会有丢失风险，建议使用/encrypt指令加密您的游戏数据");
                ClientDialogUtils.sendPopup(player,"warning","RDI账号安全","建议使用/encrypt指令加密您的游戏数据");
                return;
            }
        });
        ThreadPool.newThread(()-> {
            //天气信息
            String weatherInfo = HttpUtils.sendRequest("GET", "misc/weather", "ip=" + player.getIpAddress());
            //地址信息
            String addrInfo = weatherInfo.split("\n")[0];
            if(addrInfo.startsWith("@")){
                addrInfo=addrInfo.replace("@","");
                //写入地址信息
                RDICeleTech.ipGeoMap.put(player.getScoreboardName(),addrInfo);
            }
            TextUtils.sendChatMessage(player, weatherInfo);
            TextUtils.sendChatMessage(player, TimeUtils.getTimeChineseString()+"好,"+player.getDisplayName().getString());
        });
    }


    //指令注册
    private void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        final ObjectArrayList<RdiCommand> commands = new ObjectArrayList<>();
        commands.add(new CreateCommand());
        commands.add(new DeleteCommand());
        commands.add(new HomeCommand());
        commands.add(new InviteCommand());
        commands.add(new KickCommand());
        commands.add(new LocaCommand());
        commands.add(new SpawnCommand());
        commands.add(new TpaCommand());
        commands.add(new TpreqCommand());
        commands.add(new TpsCommand());
        commands.add(new StruCommand());
        commands.add(new EncryptCommand());
        commands.add(new SaveCommand());
        commands.add(new DragonCommand());
        commands.add(new HwSpecCommand());
        commands.add(new AfkListCommand());
        commands.add(new IpListCommand());

        for (RdiCommand cmd : commands) {
            if (cmd.getExecution() != null) {
                dispatcher.register(cmd.getExecution());
            }
        }
    }
}
