package calebzhou.rdi.core.server;

import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.command.impl.*;
import calebzhou.rdi.core.server.model.RdiGeoLocation;
import calebzhou.rdi.core.server.model.RdiPlayerLocation;
import calebzhou.rdi.core.server.model.RdiWeather;
import calebzhou.rdi.core.server.model.ResultData;
import calebzhou.rdi.core.server.module.DeathRandomDrop;
import calebzhou.rdi.core.server.utils.*;
import com.mojang.brigadier.CommandDispatcher;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
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
import org.quiltmc.qsl.lifecycle.api.event.ServerTickEvents;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayConnectionEvents;
import xyz.nucleoid.fantasy.Fantasy;

import java.io.File;
import java.util.Set;

import static calebzhou.rdi.core.server.RdiMemoryStorage.commandSet;
import static calebzhou.rdi.core.server.utils.PlayerUtils.*;


public class RdiEvents {
    public static final RdiEvents INSTANCE = new RdiEvents();
    private RdiEvents(){}
    public void register(){
		ServerTickEvents.END.register(this::onServerEndTick);
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
        ServerPlayConnectionEvents.JOIN.register(this::onPlayerJoin);
        ServerPlayConnectionEvents.DISCONNECT.register(this::onPlayerDisconnect);
		PlayerBlockBreakEvents.BEFORE.register(this::beforeBreakBlock);
        PlayerBlockBreakEvents.AFTER.register(this::onBreakBlock);
		UseBlockCallback.EVENT.register(this::onUseBlock);
        ServerPlayerEvents.ALLOW_DEATH.register(this::onPlayerDeath);
		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(this::onPlayerChangeWorld);
    }

	private void onServerEndTick(MinecraftServer server) {
		RdiTickTaskManager.onServerTick();
	}

	private InteractionResult onUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult result) {
		if(isFreshPlayer(player)&&isInMainTown(player))
			return InteractionResult.FAIL;

		return InteractionResult.PASS;

	}

	//改变世界之后
	private void onPlayerChangeWorld(ServerPlayer player, ServerLevel fromLevel, ServerLevel toLevel) {
		 IslandUtils.unloadIsland(fromLevel,player);
	}

	//破坏方块之前
	private boolean beforeBreakBlock(Level level, Player player, BlockPos pos, BlockState blockState, BlockEntity blockEntity) {
		if(isInMainTown(player)&&isFreshPlayer(player)){
				sendChatMessage(player, RESPONSE_INFO,"按下T键，输入/is create指令创建一个岛屿吧！或者让您的朋友输入/is invite "+player.getScoreboardName()+"来邀请您加入他的岛屿");
				return false;
		}
		return true;
	}

    //玩家死亡
    private boolean onPlayerDeath(ServerPlayer player, DamageSource source, float damage) {
        String pid = player.getStringUUID();
        String src = source.getMsgId();
        //记录死亡
       	RdiHttpClient.sendRequestAsyncResponseless("post","/mcs/record/death",Pair.of("pid",pid),Pair.of("src",src));
        //随机掉落物品
        DeathRandomDrop.handleDeath(player);
		//记录地点
		RdiPlayerLocation location = RdiPlayerLocation.create(player);
		sendChatMessage(player,RESPONSE_SUCCESS,"已经记录此地点为%s，使用/back指令可以返回。".formatted(location.toString()));
		RdiMemoryStorage.pidBackPos.put(pid, location);
        return true;
    }

    //玩家断开服务器
    private void onPlayerDisconnect(ServerGamePacketListenerImpl listener, MinecraftServer server) {
        ServerPlayer player = listener.getPlayer();
		//移除挂机信息和传送信息
        RdiMemoryStorage.afkMap.removeInt(player.getScoreboardName());
        RdiMemoryStorage.tpaMap.remove(player.getStringUUID());
        //记录登出信息
        RdiHttpClient.sendRequestAsyncResponseless("post","/mcs/record/logout", Pair.of("pid",player.getStringUUID()));
		IslandUtils.unloadIsland(player);
    }
    //act 0放置1破坏
    public static void recordBlock(String pid,String bid,int act,Level world,int x,int y,int z){
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
        RdiHttpClient.sendRequestAsyncResponseless("post", "/mcs/record/block",
				Pair.of("pid",pid),
				Pair.of("bid",bid),
				Pair.of("act",act),
				Pair.of("world",world),
				Pair.of("x",x),
				Pair.of("y",y),
				Pair.of("z",z)
		);
    }
    //成功破坏方块
    private void onBreakBlock(Level level, Player player, BlockPos blockPos, BlockState state, BlockEntity block) {
        //发送破坏数据
        recordBlock(player.getStringUUID(), Registry.BLOCK.getKey(state.getBlock()).toString(),1, level, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }


	/*public static void handlePlayerGrowTree(Player player, SaplingBlock saplingBlock, Level level, BlockPos blockPos){
		if(player.getHealth()<1f)
			return;
		if(!player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty())
			return;
		saplingBlock.performBonemeal((ServerLevel) level,player.getRandom(),blockPos,level.getBlockState(blockPos));
		player.hurt(DamageSource.MAGIC,19.5f);
		sendChatMessage(player, RESPONSE_INFO,"您成功给小树输了营养，现在它已经长出来了捏~");
	}*/
    //玩家连接服务器 连接成功
    private void onPlayerJoin(ServerGamePacketListenerImpl listener, PacketSender sender, MinecraftServer server) {
        ServerPlayer player = listener.getPlayer();
		//准备传送到主城
		if(RdiMemoryStorage.pidBeingGoSpawn.contains(player.getStringUUID())){
			PlayerUtils.teleportToSpawn(player);
			RdiMemoryStorage.pidBeingGoSpawn.remove(player.getStringUUID());
		}
        //提示账号是否有密码
		String pid = player.getStringUUID();
		ThreadPool.newThread(()-> {
			ResultData<Boolean> resultData = RdiHttpClient.sendRequest(Boolean.class,"get", "/v37/account/isreg/" + pid);
            if(resultData.getData()){
				if (Boolean.parseBoolean(String.valueOf(resultData.getData()))){
					ResultData loginData = RdiHttpClient.sendRequest("get", "/v37/account/login/" + pid, Pair.of("pwd", RdiMemoryStorage.pidUserMap.get(pid).getPwd()));
					if (!loginData.isSuccess()) {
						RdiCoreServer.LOGGER.info("此请求密码错误！");
						player.connection.disconnect(Component.literal("RDI密码错误，请尝试重启游戏\n或者检查文件"+PlayerUtils.getPasswordStorageFile(player)));
					}
				}
			}else{
				sendClientPopup(player,"warning","RDI账号安全","建议使用/set-password指令设置密码");
			}
        });
		ThreadPool.newThread(()->{
			ResultData<RdiGeoLocation> request = RdiHttpClient.sendRequest(RdiGeoLocation.class,"get", "/v37/public/ip2loca",Pair.of("ip",player.getIpAddress()));
			if(request.isSuccess()){
				RdiGeoLocation geoLocation = request.getData();
				ResultData<RdiWeather> weatherResultData = RdiHttpClient.sendRequest(RdiWeather.class,"get", "/v37/public/weather", Pair.of("longitude", geoLocation.location.longitude), Pair.of("latitude", geoLocation.location.latitude));
				if (weatherResultData.isSuccess()) {
					RdiWeather rdiWeather = weatherResultData.getData();
					PlayerUtils.sayHello(player);
					PlayerUtils.saveGeoLocation(player,geoLocation);
					PlayerUtils.saveWeather(player,rdiWeather);

					PlayerUtils.getAllPlayers().forEach(pl->pl.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.UPDATE_DISPLAY_NAME,player)));;
					PlayerUtils.sendWeatherInfo(player,geoLocation,rdiWeather);;
					PlayerUtils.sendTomorrowWeatherInfo(player,geoLocation,rdiWeather);
					RdiHttpClient.sendRequestAsyncResponseless("post","/mcs/record/login",
							Pair.of("pid", pid),
							Pair.of("ip", player.getIpAddress()),
							Pair.of("geo",geoLocation));
					RdiHttpClient.sendRequestAsyncResponseless("post","/mcs/record/idname",
							Pair.of("pid", pid),
							Pair.of("name" , player.getScoreboardName())
					);

				}
			}
		});
    }


    //指令注册
    private void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
		commandSet.add(new BackCommand());
		commandSet.add(new ChangeBiomeCommand());
		commandSet.add(new DragonCommand());
		commandSet.add(new GoNetherCommand());
		commandSet.add(new HelpCommand());
		commandSet.add(new Home1Command());
		commandSet.add(new HomeCommand());
		commandSet.add(new IslandCommand());
		commandSet.add(new MeltObsidianCommand());
		commandSet.add(new RdiConfirmCommand());
		commandSet.add(new SaveCommand());
		commandSet.add(new SetPasswordCommand());
		commandSet.add(new SpawnCommand());
		commandSet.add(new SpeakCommand());
		commandSet.add(new TpaCommand());
		commandSet.add(new TpsCommand());
		commandSet.add(new TpyesCommand());
		commandSet.add(new WeatherCommand());
		commandSet.forEach(cmd->{
			if (cmd.getExecution() != null) {
				dispatcher.register(cmd.getExecution());
			}
		});
    }
}
