package calebzhou.rdi.core.server;

import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.command.impl.*;
import calebzhou.rdi.core.server.module.DeathRandomDrop;
import calebzhou.rdi.core.server.thread.RdiHttpPlayerRequest;
import calebzhou.rdi.core.server.thread.RdiHttpRequest;
import calebzhou.rdi.core.server.thread.RdiRequestThread;
import calebzhou.rdi.core.server.thread.RdiSendRecordThread;
import calebzhou.rdi.core.server.utils.*;
import com.mojang.brigadier.CommandDispatcher;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayConnectionEvents;
import xyz.nucleoid.fantasy.Fantasy;

import java.io.File;

import static calebzhou.rdi.core.server.utils.PlayerUtils.*;


public class RdiEvents {
    public static final RdiEvents INSTANCE = new RdiEvents();
    private RdiEvents(){}
    public void register(){
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
        ServerPlayConnectionEvents.JOIN.register(this::onPlayerJoin);
        ServerPlayConnectionEvents.DISCONNECT.register(this::onPlayerDisconnect);
		PlayerBlockBreakEvents.BEFORE.register(this::beforeBreakBlock);
        PlayerBlockBreakEvents.AFTER.register(this::onBreakBlock);
        UseBlockCallback.EVENT.register(this::onRightClickBlock);
        ServerMessageEvents.CHAT_MESSAGE.register(this::onPlayerChat);
        ServerPlayerEvents.ALLOW_DEATH.register(this::onPlayerDeath);
    }

	//破坏方块之前
	private boolean beforeBreakBlock(Level level, Player player, BlockPos pos, BlockState blockState, BlockEntity blockEntity) {
		if(isInOverworld(player)){
			if(isFreshPlayer(player)){
				sendChatMessage(player, RESPONSE_INFO,"按下T键，输入/is create指令创建一个岛屿吧！或者让您的朋友输入/is invite "+player.getScoreboardName()+"来邀请您加入他的岛屿");
				return false;
			}
		}
		return true;
	}

	private void recordChat(String pid, String cont){
        RdiSendRecordThread.addTask(new RdiHttpRequest(RdiHttpRequest.Type.post,"mcs/record/chat","pid="+pid,"cont="+ EncodingUtils.getUTF8StringFromGBKString(cont)));
    }
    //玩家聊天
    private void onPlayerChat(PlayerChatMessage text, ServerPlayer player, ChatType.Bound bound) {
        recordChat(player.getStringUUID(), text.unsignedContent().get().getString());
    }

    //玩家死亡
    private boolean onPlayerDeath(ServerPlayer player, DamageSource source, float damage) {
        String pid = player.getStringUUID();
        String src = source.getMsgId();
        //记录死亡
        RdiSendRecordThread.addTask(new RdiHttpRequest(RdiHttpRequest.Type.post,"mcs/record/death","pid="+pid,"src="+src));

        //随机掉落物品
        DeathRandomDrop.handleDeath(player);
        return true;
    }

    //玩家断开服务器
    private void onPlayerDisconnect(ServerGamePacketListenerImpl listener, MinecraftServer server) {
        ServerPlayer player = listener.getPlayer();
		//移除挂机信息和传送信息
        RdiMemoryStorage.afkMap.removeInt(player.getScoreboardName());
        RdiMemoryStorage.tpaMap.remove(player.getStringUUID());
        //记录登出信息
        RdiSendRecordThread.addTask(new RdiHttpRequest(RdiHttpRequest.Type.post,"mcs/record/logout", "pid="+player.getStringUUID()));
		//如果在二岛
		ServerLevel islandLevel = player.getLevel();
		if(WorldUtils.isInIsland2(islandLevel)){
			//如果岛上没有人 就卸载存档
			if(islandLevel.getPlayers(p-> true).isEmpty()){
				RdiCoreServer.LOGGER.info("岛屿"+WorldUtils.getDimensionName(islandLevel)+"没有玩家了，即将卸载");
				Fantasy.get(RdiCoreServer.getServer()).unloadWorld(islandLevel);
			}
		}
    }

    //act 0放置1破坏
    private void recordBlock(String pid,String bid,int act,Level world,int x,int y,int z){
        //主世界主城以外的地方不记录
        if(world == RdiCoreServer.getServer().overworld()){
            boolean rangeInSpawn = (x > -256 && x < 256) && (z > -256 && z < 256);
            if(!rangeInSpawn){
                return;
            }
        }
        //末地 地狱不记录
        if(world == RdiCoreServer.getServer().getLevel(Level.NETHER) || world == RdiCoreServer.getServer().getLevel(Level.END))
            return;
        RdiSendRecordThread.addTask(new RdiHttpRequest(RdiHttpRequest.Type.post, "mcs/record/block", "pid="+pid,"bid="+bid,"act="+act,"world="+world,"x="+x,"y="+y,"z="+z));
    }
    //成功破坏方块
    private void onBreakBlock(Level level, Player player, BlockPos blockPos, BlockState state, BlockEntity block) {
        //发送破坏数据
        recordBlock(player.getStringUUID(), Registry.BLOCK.getKey(state.getBlock()).toString(),1, level, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    //放置方块
    private InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult result) {
        BlockPos blockPos = result.getBlockPos();
        Block block = level.getBlockState(blockPos).getBlock();
		if(block instanceof SaplingBlock saplingBlock){
			handlePlayerGrowTree(player,saplingBlock,level,blockPos);
		}
        //发送放置数据
        recordBlock(player.getStringUUID(), Registry.BLOCK.getKey(block).toString(),0,level, blockPos.getX(), blockPos.getY(), blockPos.getZ());
        return InteractionResult.PASS;
    }
	public static void handlePlayerGrowTree(Player player, SaplingBlock saplingBlock, Level level, BlockPos blockPos){
		if(player.getHealth()<1f)
			return  ;
		saplingBlock.performBonemeal((ServerLevel) level,player.getRandom(),blockPos,level.getBlockState(blockPos));
		sendChatMessage(player, RESPONSE_INFO,"您成功给小树输了血，现在它已经长出来了捏~");
		player.setHealth(player.getHealth()*0.1f);
	}
    //玩家连接服务器 连接成功
    private void onPlayerJoin(ServerGamePacketListenerImpl listener, PacketSender sender, MinecraftServer server) {
        ServerPlayer player = listener.getPlayer();
        //提示账号是否有密码
        ThreadPool.newThread(()-> {
            File pwdFile = getPasswordFile(player);
            if(!pwdFile.exists()){
                sendChatMessage(player, RESPONSE_INFO,"您的账号数据尚未加密，可能会有丢失风险，建议使用/encrypt指令加密您的游戏数据");
                sendClientPopup(player,"warning","RDI账号安全","建议使用/encrypt指令加密您的游戏数据");
                return;
            }
        });
        //发送天气预报
        RdiRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.get,
                player,
                weatherInfo->{
					weatherInfo.getData()
                    //地址信息
                    String addrInfo = weatherInfo.split("\n")[0];
                    if(addrInfo.startsWith("@")){
                        addrInfo=addrInfo.replace("@","");
                        //写入地址信息
                        RdiMemoryStorage.ipGeoMap.put(player.getScoreboardName(),addrInfo);
                    }
                    //玩家不显示第一行地址信息
                    weatherInfo = weatherInfo.replace(addrInfo,"").replace("@","");
                    sendChatMessage(player, weatherInfo);
                    sendChatMessage(player, TimeUtils.getTimeChineseString()+"好,"+player.getDisplayName().getString());
                    //发送登录记录
                    RdiSendRecordThread.addTask(new RdiHttpRequest(RdiHttpRequest.Type.post,"mcs/record/login","pid="+player.getStringUUID(),"ip=" + player.getIpAddress(),"geo="+addrInfo));
                    //发送id昵称信息
                    RdiSendRecordThread.addTask(new RdiHttpRequest(RdiHttpRequest.Type.post,"mcs/record/idname","pid="+player.getStringUUID(),"name=" + player.getScoreboardName()));

                },
                "misc/weather?ip="+ player.getIpAddress())
        );
        //查询是否有岛屿，如果没有就提示创建
        ThreadPool.newThread(()->{
        RdiRequestThread.addTask(new RdiHttpPlayerRequest(
                RdiHttpRequest.Type.get,
                player,
                resp->{
                    if(resp.equals("fail")){
                        sendChatMessage(player, RESPONSE_INFO,"您没有加入任何岛屿，输入/is2 create指令立刻创建一个吧！");
                    }
                },
                "island/" + player.getStringUUID()
        ));});
    }


    //指令注册
    private void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        final ObjectArrayList<RdiCommand> commands = new ObjectArrayList<>();
        commands.add(new IslandCommand());
        commands.add(new SpawnCommand());
        commands.add(new TpaCommand());
        commands.add(new TpreqCommand());
        commands.add(new TpsCommand());
        commands.add(new EncryptCommand());
        commands.add(new SaveCommand());
        commands.add(new DragonCommand());
        commands.add(new HwSpecCommand());
        commands.add(new IpListCommand());
        commands.add(new Home1Command());
        commands.add(new HomeCommand());
        commands.add(new TickInverterCommand());
        commands.add(new HelpCommand());
        commands.add(new ChangeBiomeCommand());
        commands.add(new GoNetherCommand());

        for (RdiCommand cmd : commands) {
            if (cmd.getExecution() != null) {
                dispatcher.register(cmd.getExecution());
            }
        }
    }
}
