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
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.io.File;

public class FabricEventRegister {


    public FabricEventRegister(){
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
        ServerPlayConnectionEvents.JOIN.register(this::onPlayerJoin);
        ServerPlayConnectionEvents.DISCONNECT.register(this::onPlayerDisconnect);
        PlayerBlockBreakEvents.AFTER.register(this::onBreakBlock);
        UseBlockCallback.EVENT.register(this::onPlaceBlock);
        ServerMessageEvents.CHAT_MESSAGE.register(this::onPlayerChat);
        ServerMessageEvents.COMMAND_MESSAGE.register(this::onPlayerCommand);
        ServerPlayerEvents.ALLOW_DEATH.register(this::onPlayerDeath);
    }



    private void recordChatCommand(String pid,String cont){
        ThreadPool.newThread(()->{
            HttpUtils.sendRequest("post","record/chat","pid="+pid,"cont="+EncodingUtils.getUTF8StringFromGBKString(cont));
        });
    }
    //玩家聊天
    private void onPlayerChat(FilteredText<PlayerChatMessage> text, ServerPlayer player, ResourceKey<ChatType> key) {
        recordChatCommand(player.getStringUUID(), text.raw().serverContent().getString());
    }
    //玩家使用指令
    private void onPlayerCommand(FilteredText<PlayerChatMessage> text, CommandSourceStack stack, ResourceKey<ChatType> key) {
        ServerPlayer player = stack.getPlayer();
        if(player != null)
            recordChatCommand(player.getStringUUID(), text.raw().serverContent().getString());
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
        //发送登出信息
        ThreadPool.newThread(()->{
            HttpUtils.sendRequest("post", "record/logout", "pid="+player.getStringUUID());
        });
    }

    //act 0放置1破坏
    private void recordBlock(String pid,String bid,int act,String world,int x,int y,int z){
        HttpUtils.sendRequest("post", "record/block", "pid="+pid,"bid="+bid,"act="+act,"world="+world,"x="+x,"y="+y,"z="+z);
    }
    //成功破坏方块
    private void onBreakBlock(Level level, Player player, BlockPos blockPos, BlockState state, BlockEntity block) {
        //发送破坏数据
        ThreadPool.newThread(()->{
            recordBlock(player.getStringUUID(), Registry.BLOCK.getKey(state.getBlock()).toString(),1,level.dimension().location().toString(), blockPos.getX(), blockPos.getY(), blockPos.getZ());
        });
    }

    //放置方块
    private InteractionResult onPlaceBlock(Player player, Level level, InteractionHand hand, BlockHitResult result) {
        BlockPos blockPos = result.getBlockPos();
        Block block = level.getBlockState(blockPos).getBlock();
        //发送放置数据
        ThreadPool.newThread(()->{
            recordBlock(player.getStringUUID(), Registry.BLOCK.getKey(block).toString(),0,level.dimension().location().toString(), blockPos.getX(), blockPos.getY(), blockPos.getZ());
        });
        return InteractionResult.PASS;
    }
    //玩家连接服务器 连接成功
    private void onPlayerJoin(ServerGamePacketListenerImpl listener, PacketSender sender, MinecraftServer server) {
        ServerPlayer player = listener.getPlayer();
        //提示账号是否有密码
        ThreadPool.newThread(()-> {
            File pwdFile = PlayerUtils.getPasswordFile(player);
            if(!pwdFile.exists()){
                TextUtils.sendChatMessage(player, MessageType.INFO,"您的账号数据尚未加密，可能会有丢失风险，建议使用/encrypt指令加密您的游戏数据");
                ClientDialogUtils.sendPopup(player,"warning","RDI账号安全","建议使用/encrypt指令加密您的游戏数据");
                return;
            }
        });
        //发送天气预报
        ThreadPool.newThread(()-> {
            //天气信息
            String weatherInfo = HttpUtils.sendRequest("get", "misc/weather", "ip=" + player.getIpAddress());
            //地址信息
            String addrInfo = weatherInfo.split("\n")[0];
            if(addrInfo.startsWith("@")){
                addrInfo=addrInfo.replace("@","");
                //写入地址信息
                RDICeleTech.ipGeoMap.put(player.getScoreboardName(),addrInfo);
            }
            //玩家不显示第一行地址信息
            weatherInfo = weatherInfo.replace(addrInfo,"");
            TextUtils.sendChatMessage(player, weatherInfo);
            TextUtils.sendChatMessage(player, TimeUtils.getTimeChineseString()+"好,"+player.getDisplayName().getString());
            //发送登录记录
            HttpUtils.sendRequest("post", "record/login", "pid="+player.getStringUUID(),"ip=" + player.getIpAddress(),"geo="+addrInfo);

        });

        //发送id昵称信息
        ThreadPool.newThread(()->{
            HttpUtils.sendRequest("post", "record/idname", "pid="+player.getStringUUID(),"name=" + player.getScoreboardName());
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