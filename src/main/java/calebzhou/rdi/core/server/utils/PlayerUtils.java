package calebzhou.rdi.core.server.utils;

import calebzhou.rdi.core.server.NetworkPackets;
import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.RdiMemoryStorage;
import calebzhou.rdi.core.server.RdiSharedConstants;
import calebzhou.rdi.core.server.constant.ColorConst;
import calebzhou.rdi.core.server.constant.FileConst;
import calebzhou.rdi.core.server.constant.WeatherConst;
import calebzhou.rdi.core.server.model.RdiGeoLocation;
import calebzhou.rdi.core.server.model.RdiPlayerLocation;
import calebzhou.rdi.core.server.model.RdiWeather;
import calebzhou.rdi.core.server.model.ResultData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static calebzhou.rdi.core.server.RdiSharedConstants.SPAWN_LOCATION;

public class PlayerUtils {
	public static final int RESPONSE_SUCCESS=2;
	public static final int RESPONSE_WARNING=1;
	public static final int RESPONSE_INFO=0;
	public static final int RESPONSE_ERROR=-1;
	public static final String RESPONSE_ERROR_PREFIX = "%s%s错误 >%s%s ".formatted(ColorConst.DARK_RED,ColorConst.BOLD,ColorConst.RESET,ColorConst.RED);
	public static final String RESPONSE_SUCCESS_PREFIX = "%s%s成功 >%s%s ".formatted(ColorConst.DARK_GREEN,ColorConst.BOLD,ColorConst.RESET,ColorConst.BRIGHT_GREEN);
	public static final String RESPONSE_INFO_PREFIX = "%s%s提示 >%s%s ".formatted(ColorConst.AQUA,ColorConst.BOLD,ColorConst.RESET,ColorConst.AQUA);
	public static final String RESPONSE_WARNING_PREFIX = "%s%s警告 >%s%s ".formatted(ColorConst.GOLD,ColorConst.BOLD,ColorConst.RESET,ColorConst.GOLD);

	public static void sendPacketToClient(Player player, ResourceLocation pack, Object content) {
		FriendlyByteBuf buf = PacketByteBufs.create();
		if(content instanceof Integer i)
			buf.writeInt(i);
		else if(content instanceof Double i)
			buf.writeDouble(i);
		else if(content instanceof Float i)
			buf.writeFloat(i);
		else if(content instanceof Long i)
			buf.writeLong(i);
		else if(content instanceof CompoundTag i)
			buf.writeNbt(i);
		else
			buf.writeUtf(content+"");
		ServerPlayNetworking.send((ServerPlayer) player,pack,buf);
	}


	public static List<ServerPlayer> getPlayersInLevel(Level level){
		return getPlayersInLevel(level,p->true);
	}
	public static List<ServerPlayer> getPlayersInLevel(Level level, Predicate<? super ServerPlayer> condition){
		return ((ServerLevel) level).getPlayers(condition);
	}
	public static void sendBossBar(Player player,Component component,BossEvent.BossBarColor bossBarColor, BossEvent.BossBarOverlay bossBarOverlay){
		ClientboundBossEventPacket addPacket = ClientboundBossEventPacket.createAddPacket(new ServerBossEvent(component, bossBarColor, bossBarOverlay));
		((ServerPlayer)player).connection.send(addPacket);
		addPacket=null;
	}
	public static void broadcastMessageToLevel(ServerLevel level,Component component,boolean actionBar){
		getPlayersInLevel(level).forEach(player -> {
			sendChatMessage(player,component,actionBar);
		});
	}
	public static void broadcastMessageToLevel(Level level,Component component,boolean actionBar){
		broadcastMessageToLevel((ServerLevel) level,component,actionBar);
	}
	public static void sendMessageToCommandSource(CommandSourceStack source , String content){
		sendMessageToCommandSource(source,Component.literal(content));
	}
	public static void sendMessageToCommandSource(CommandSourceStack source, Component textComponent) {
		source.sendSuccess(textComponent,false);
	}

	public static boolean isStandOnBlock(Player player, Block block){
		BlockPos onPos = player.getOnPos();
		return  block == player.getLevel().getBlockState(onPos).getBlock();
	}
	public static void sendChatMessage(Player player, Component textComponent, boolean isOnActionBar) {
		player.displayClientMessage(textComponent, isOnActionBar);
	}
	public static void sendServiceResultData(Player player, ResultData data) {
		sendChatMessage(player,data.getStatus()>0?RESPONSE_SUCCESS:RESPONSE_ERROR,data.getMessage());
	}
	public static void sendChatMessage(Player player, Component textComponent) {
		sendChatMessage(player,textComponent ,false);
	}
	public static void sendChatMessage(Player player, String content) {
		sendChatMessage(player,Component.literal(content));
	}
	public static void sendChatMessage(Player player, int messageType) {
		sendChatMessage(player,messageType,"");
	}
	public static void sendChatMessage(Player player, int messageType, String content ){
		switch (messageType){
			case RESPONSE_SUCCESS -> sendChatMessage(player,RESPONSE_SUCCESS_PREFIX+content);
			case RESPONSE_WARNING -> sendChatMessage(player,RESPONSE_WARNING_PREFIX+content);
			case RESPONSE_INFO -> sendChatMessage(player,RESPONSE_INFO_PREFIX+content);
			case RESPONSE_ERROR -> sendChatMessage(player,RESPONSE_ERROR_PREFIX+content);
		}
	}

	public static void sendChatMultilineMessage(Player player,String content){
		String[] split = content.split("\n");
		for(String line:split){
			sendChatMessage(player,line);
		}
	}

	//添加生物效果
    public static void addEffect(ServerPlayer player, MobEffect effect,int seconds,int amplifier){
        player.addEffect(new MobEffectInstance(effect,seconds*20,amplifier));
    }
    public static File getPasswordFile(ServerPlayer player)
    {
        return getPasswordFile(player.getStringUUID());
    }
    public static File getPasswordFile(String playerUuid){
        return  new File(FileConst.getPasswordFolder(),playerUuid+".txt");
    }

	//是否是新手
	public static boolean isFreshPlayer(Player player){
		return player.experienceLevel==0;
	}
	//是否在主城
	public static boolean isInMainTown(Player player){
		BlockPos onPos = player.getOnPos();
		return  (onPos.getX() > -256 && onPos.getX() < 256)
				&&
				(onPos.getZ() > -256 && onPos.getZ() < 256)
				&& isInOverworld(player);
	}
	/**
	 * 是否在主世界
	 */
	public static boolean isInOverworld(Player player){
		return player.getLevel() == RdiCoreServer.getServer().overworld();
	}
	/**
	 * 是否在末地
	 */
	public static boolean isInTheEnd(Player player){
		return player.getLevel() == RdiCoreServer.getServer().getLevel(Level.END);
	}
	/**
	 * 是否在地狱
	 */
	public static boolean isInNether(Player player){
		return player.getLevel() == RdiCoreServer.getServer().getLevel(Level.NETHER);
	}
	/**
	 * 是否在岛屿世界
	 */
	public static boolean isInIsland(Player player){
		return WorldUtils.getDimensionName(player.getLevel())
				.startsWith(RdiSharedConstants.ISLAND_DIMENSION_FULL_PREFIX);
	}
	public static void sendClientPopup(Player player,String type, String title, String msg){
		sendPacketToClient(player, NetworkPackets.POPUP,String.format("%s|%s|%s",type,title,msg));
	}
	public static void sendClientDialog(Player player,String type, String title, String msg){
		sendPacketToClient(player, NetworkPackets.DIALOG_INFO,String.format("%s|%s|%s",type,title,msg));
	}
	public static void teleportToSpawn(Player player){
		teleport(player, RdiCoreServer.getServer().overworld(),RdiSharedConstants.SPAWN_LOCATION);
	}
	public static void teleport(Player player,ServerLevel lvl,BlockPos bpos){
		teleport(player,lvl,bpos.getX(), bpos.getY(), bpos.getZ(),0,0);
	}
	//传送1到2 玩家
	public static void teleport(Player player1, Player player2){
		teleport(player1, (ServerLevel) player2.getLevel(), player2.getX(),
				player2.getY(), player2.getZ(), player2.getYRot(), player2.getXRot());
	}
	public static void teleport(Player player, RdiPlayerLocation playerLocation){
		teleport(player,
				playerLocation.getLevel(),
				playerLocation.getX(),
				playerLocation.getY(),
				playerLocation.getZ(),
				playerLocation.getW(),
				playerLocation.getP())
		;
	}
    //传送到指定位置
    public static void teleport(Player player, ServerLevel world, double x, double y, double z, double yaw, double pitch){
        if(world==null)
            world= RdiCoreServer.getServer().overworld();
        BlockPos blockPos = new BlockPos(x, y, z);
        float warpYaw = (float) Mth.wrapDegrees(yaw);
        float warpPitch = (float) Mth.wrapDegrees(pitch);
            ChunkPos chunkPos = new ChunkPos(blockPos);
            world.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkPos, 1, player.getId());
            player.stopRiding();
            if (player.isSleeping()) {
                player.stopSleepInBed(true, true);
            }
            if (world == player.level) {
                ((ServerPlayer)player).connection.teleport(x, y, z,  warpPitch,warpYaw, EnumSet.noneOf(ClientboundPlayerPositionPacket.RelativeArgument.class));
            } else {
                ((ServerPlayer)player).teleportTo(world, x, y, z,  warpPitch,warpYaw);
            }
        player.setYHeadRot(warpYaw);
    }
    public static BlockPos getPlayerLookingBlockPosition(Player player, boolean isFluid){
        BlockHitResult rays=(BlockHitResult) player.pick(64.0D,0.0f,isFluid);
        return rays.getBlockPos();
    }
    public static ServerPlayer getPlayerByName(String name){
        return RdiCoreServer.getServer().getPlayerList().getPlayerByName(name);
    }
    public static ServerPlayer getPlayerByUuid(String uuid){
        return getPlayerByUuid(UUID.fromString(uuid));
    }
    public static ServerPlayer getPlayerByUuid(UUID uuid){
        return RdiCoreServer.getServer().getPlayerList().getPlayer(uuid);
    }
    public static void setSpawnPoint(ServerPlayer player, ResourceKey<Level> levelResourceKey,BlockPos blockPos){
        player.setRespawnPosition(levelResourceKey,blockPos,0,true,true);
    }
    public static void resetProfile(ServerPlayer player){
        player.experienceLevel=0;
        player.getInventory().clearContent();
        player.kill();
		PlayerUtils.teleport(player,RdiCoreServer.getServer().overworld(), SPAWN_LOCATION);
        PlayerUtils.setSpawnPoint(player, Level.OVERWORLD, SPAWN_LOCATION);
		//player.connection.disconnect(Component.literal("成功重置您的存档！请重新连接服务器。"));
    }

	public static void teleport(ServerPlayer player, ServerLevel level, Vec3 vec3) {
		teleport(player, level, vec3.x, vec3.y, vec3.z, 0f,0f);
	}
	//保存玩家的地址记录
	public static void saveGeoLocation(ServerPlayer player, RdiGeoLocation geoLocation) {
		RdiMemoryStorage.pidGeoMap.put(player.getStringUUID(),geoLocation);
	}

	public static void sendWeatherInfo(ServerPlayer player, RdiGeoLocation geoLocation, RdiWeather rdiWeather) {
		String alert = ColorConst.GOLD+rdiWeather.alert+ColorConst.RESET;
		String loca = geoLocation.city.replace("市","")+"-"+geoLocation.district.replace("区", "").replace("市", "");;
		String tempNow = Math.floor(rdiWeather.temperature)+"℃";
		String humidity = "湿度"+Math.floor(rdiWeather.humidity*100)+"%";
		String skycon = WeatherConst.valueOf(rdiWeather.skycon).getName();
		String airQuality = "空气"+rdiWeather.aqiChn+"("+rdiWeather.aqi+")";
		/*String visibility = "能见度"+rdiWeather.visibility+"km";
		String windSpeed = "风速"+rdiWeather.windSpeed+"km/h";
		String pressure = "大气压"+rdiWeather.pressure/1000+"kPa";*/
		String rain = "降水概率"+Math.floor(rdiWeather.rainProba*100)+"% ";
		String rainChn = rdiWeather.rainDescr.replace("小彩云","dav").replace("彩云","dav");
		String hourlyDescr = rdiWeather.hourlyDescr;
		String sunRiseTime = "日出"+rdiWeather.sunRiseTime;
		String sunSetTime = "日落"+rdiWeather.sunSetTime;
		sendChatMessage(player,alert);
		sendChatMessage(player,"%s %s %s %s %s %s %s %s %s %s".formatted(loca,tempNow,skycon,hourlyDescr,airQuality,humidity,rain,rdiWeather.rainProba>0.001?rainChn:"",sunRiseTime,sunSetTime));
	}
	public static void sendTomorrowWeatherInfo(ServerPlayer player, RdiGeoLocation geoLocation, RdiWeather rdiWeather) {

	}
	public static String getPasswordStorageFile(Player player){
		return ".minecraft/mods/rdi/users/"+player.getStringUUID()+"_password.txt";
	}
	public static void sayHello(ServerPlayer player) {
		sendChatMessage(player, TimeUtils.getTimeChineseString()+"好,"+player.getDisplayName().getString());
	}


	public static void saveWeather(ServerPlayer player, RdiWeather rdiWeather) {
		RdiMemoryStorage.pidWeatherMap.put(player.getStringUUID(),rdiWeather);
	}
	public static List<ServerPlayer> getAllPlayers(){
		return RdiCoreServer.getServer().getPlayerList().getPlayers();
	}
}
