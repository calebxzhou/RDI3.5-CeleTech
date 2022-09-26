package calebzhou.rdi.core.server.command.impl;

import calebzhou.rdi.core.server.RdiCommandConfirmer;
import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.model.ResultData;
import calebzhou.rdi.core.server.utils.*;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import xyz.nucleoid.fantasy.Fantasy;
import xyz.nucleoid.fantasy.RuntimeWorldHandle;

import java.util.UUID;

import static calebzhou.rdi.core.server.utils.IslandUtils.getIslandDimensionLoca;
import static calebzhou.rdi.core.server.utils.PlayerUtils.*;

public class IslandCommand extends RdiCommand {
	public IslandCommand() {
        super("is","岛屿菜单。");
    }
    static final String islandHelp = """
            =====RDI空岛v2管理菜单=====
            指令参数
            create 创建岛屿 reset 删除岛屿 kick 移除成员 invite 邀请成员 loca 更改传送点
            transfer 改变岛主 quit 退出加入的岛屿 info 岛屿信息
            ====================
            """;


    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> sendIslandHelp(context.getSource().getPlayer()))
                .then(
                        Commands.argument("指令参数", StringArgumentType.string())
                                .suggests(
                                        (context, builder) ->
                                                SharedSuggestionProvider.suggest(new String[]{
                                                        "create","reset","kick","invite","loca","transfer","quit"},builder)
                                )
                                .executes(
                                        context -> handleSubCommand(context.getSource().getPlayer(),StringArgumentType.getString(context,"指令参数"))
                                )
                                .then(
                                        Commands.argument("玩家名", EntityArgument.player())
                                                .executes(context -> handleSubCommandWithPlayerNameParam(StringArgumentType.getString(context,"指令参数"),context.getSource().getPlayer(),EntityArgument.getPlayer(context,"玩家名")))
                                )
                )
                ;
    }
    private int handleSubCommandWithPlayerNameParam(String param, ServerPlayer fromPlayer, ServerPlayer toPlayer) {
        switch (param){
            case "invite" -> invitePlayer(fromPlayer,toPlayer);
            case "kick" -> kickPlayer(fromPlayer,toPlayer);
            case "transfer" -> transferIsland(fromPlayer,toPlayer);
            default -> sendIslandHelp(fromPlayer);
        }
        return 1;
    }
    private int handleSubCommand(ServerPlayer player, String param) {
        switch (param){
            case "create" -> createIsland(player);
            case "reset" -> resetIsland(player);
            case "loca" -> locateIsland(player);
            case "quit" -> quitIsland(player);
            default -> sendIslandHelp(player);
        }
        return 1;
    }
	private int sendIslandHelp(ServerPlayer player) {
		sendChatMultilineMessage(player,islandHelp);
		return 1;
	}

	private void resetIsland(ServerPlayer player){
		sendChatMessage(player,RESPONSE_WARNING,"真的，要重置这个岛屿吗？所有的数据将会被删除！");
		String cmdId = UUID.randomUUID().toString();
		RdiCommandConfirmer.put(cmdId,this::confirmedResetIsland);
		sendChatMessage(player,RESPONSE_WARNING,"要确认重置，请输入指令：/rdi-confirm "+cmdId);
	}
	private void confirmedResetIsland(ServerPlayer player) {
		ThreadPool.newThread(()->{
			ResultData<Integer> resultData = RdiHttpClient.sendRequest(Integer.class,"delete", "/v37/island2/" + player.getStringUUID());
			if(resultData.isSuccess()){
				ResourceLocation dim = IslandUtils.getIslandDimensionLoca(resultData.getData());
				ServerUtils.executeOnServerThread(()-> {
					RuntimeWorldHandle worldHandle = Fantasy.get(RdiCoreServer.getServer()).getOrOpenPersistentWorld(dim, IslandUtils.getIslandWorldConfig());
					resetProfile(player);
					worldHandle.delete();
					sendChatMessage(player, RESPONSE_SUCCESS);
				});
			}
		});

	}


	private void quitIsland(ServerPlayer player){
		sendChatMessage(player,RESPONSE_WARNING,"真的，要退出这个岛屿吗？您的个人全部数据，将会被删除！");
		String cmdId = UUID.randomUUID().toString();
		RdiCommandConfirmer.put(cmdId,this::confirmedQuitIsland);
		sendChatMessage(player,RESPONSE_WARNING,"要确认退出，请输入指令：/rdi-confirm "+cmdId);
	}
    private void confirmedQuitIsland(ServerPlayer player) {
		ThreadPool.newThread(()->{
			ResultData resultData = RdiHttpClient.sendRequest("delete", "/island2/crew/" + player.getStringUUID());
			if (resultData.isSuccess()) {
				ServerUtils.executeOnServerThread(()-> {
					resetProfile(player);
					sendChatMessage(player,RESPONSE_SUCCESS);
				});
			}else sendServiceResultData(player,resultData);
		});
    }

	private void transferIsland(ServerPlayer fromPlayer,ServerPlayer toPlayer){
		if(fromPlayer==toPlayer){
			sendChatMessage(fromPlayer, RESPONSE_ERROR,"目标玩家不能是自己！");
			return;
		}
		sendChatMessage(fromPlayer,RESPONSE_WARNING,"真的，要转让这个岛屿吗？您的个人全部数据，将会被删除！");
		String cmdId = UUID.randomUUID().toString();
		RdiCommandConfirmer.put(cmdId,
				fromPlayerAfterConfirm ->
						confirmedTransferToPlayer(fromPlayerAfterConfirm,toPlayer));
		sendChatMessage(fromPlayer,RESPONSE_WARNING,"要确认转让，请输入指令：/rdi-confirm "+cmdId);
	}
    private void confirmedTransferToPlayer(ServerPlayer fromPlayer, ServerPlayer toPlayer) {
		ThreadPool.newThread(()->{
			ResultData resultData = RdiHttpClient.sendRequest("put", "/v37/island2/transfer/" + fromPlayer.getStringUUID() + "/" + toPlayer.getStringUUID());
			if(resultData.isSuccess()){
				sendChatMessage(fromPlayer, RESPONSE_SUCCESS);
				sendChatMessage(toPlayer, RESPONSE_SUCCESS, fromPlayer.getScoreboardName()+"把岛屿转让给了你！");
			}else sendServiceResultData(fromPlayer,resultData);
		});
    }

    private void createIsland(ServerPlayer player){
        sendChatMessage(player, RESPONSE_INFO,"准备创建岛屿，不要触碰鼠标或者键盘！");
		ThreadPool.newThread(()->{
			ResultData<Integer> resultData = RdiHttpClient.sendRequest(Integer.class,"post", "/v37/island2/" + player.getStringUUID());
			if(!resultData.isSuccess()){
				sendServiceResultData(player,resultData);
				return;
			}
			sendChatMessage(player, RESPONSE_INFO,"开始创建岛屿，不要触碰鼠标或者键盘！");
			int iid =resultData.getData();
			sendChatMessage(player, RESPONSE_INFO,"您的岛屿ID："+iid);
			MinecraftServer server = RdiCoreServer.getServer();
			ServerUtils.executeOnServerThread(()->{
				Fantasy fantasy = Fantasy.get(server);
				sendChatMessage(player, RESPONSE_INFO,"正在创建存档。。不要触碰鼠标或者键盘！");
				ResourceLocation islandDimension = getIslandDimensionLoca(iid);
				RuntimeWorldHandle worldHandle = fantasy.getOrOpenPersistentWorld(islandDimension, IslandUtils.getIslandWorldConfig());
				ServerLevel level = worldHandle.asWorld();
				IslandUtils.placeInitialBlocks(level);
				addEffect(player, MobEffects.SLOW_FALLING,10,2);
				setSpawnPoint(player,level.dimension(),WorldUtils.INIT_POS.above(2));
				sendChatMessage(player, RESPONSE_INFO,"准备传送。。。");
				teleport(player,level, Vec3.atCenterOf(WorldUtils.INIT_POS).add(0,7,0));
				sendChatMessage(player, RESPONSE_SUCCESS,"成功！");
			});
		});
    }
    private void locateIsland(ServerPlayer player) {
        if(!isInIsland(player)){
			sendChatMessage(player,RESPONSE_ERROR,"必须在您自己的岛屿上，才能设定传送点！");
			return;
		}
		if(!isStandOnBlock(player, Blocks.OBSIDIAN)){
			sendChatMessage(player,RESPONSE_ERROR,"必须在一块黑曜石上，才能设定传送点！");
			return;
		}
		sendChatMessage(player,RESPONSE_WARNING,"真的要，将这个岛屿的传送点，更改为您目前所在的位置，（%s）吗？"
				.formatted(player.getOnPos().toShortString()));
		String cmdId = UUID.randomUUID().toString();
		RdiCommandConfirmer.put(cmdId,this::confirmLocateIsland);
		sendChatMessage(player,RESPONSE_WARNING,"要确认改变传送点，请保持原地不动，输入指令：/rdi-confirm "+cmdId);
    }
	private void confirmLocateIsland(ServerPlayer player){
		double x = player.getX();
		double y = player.getY();
		double z = player.getZ();
		double w = player.getXRot();
		double p = player.getYRot();
		ThreadPool.newThread(()->{
			ResultData resultData = RdiHttpClient.sendRequest("put", "/v37/island2/loca/" + player.getStringUUID(),
					Pair.of("x", x),
					Pair.of("y", y),
					Pair.of("z", z),
					Pair.of("w", w),
					Pair.of("p", p)
			);
			if(resultData.isSuccess()){
				sendChatMessage(player,RESPONSE_SUCCESS,"将这个岛屿的传送点，更改为您目前所在的位置了！");
			}else sendServiceResultData(player,resultData);
		});
	}
    private void kickPlayer(ServerPlayer fromPlayer, ServerPlayer kickPlayer) {
        if(fromPlayer==kickPlayer){
            sendChatMessage(fromPlayer, RESPONSE_ERROR,"您不可以踢出自己!!");
            return;
        }
		sendChatMessage(fromPlayer,RESPONSE_WARNING,"真的，要踢出玩家"+kickPlayer.getScoreboardName()+"吗？他的全部数据将会被删除！");
		String cmdId = UUID.randomUUID().toString();
		RdiCommandConfirmer.put(cmdId,
				fromPlayerAfterConfirm ->
						confirmKickPlayer(fromPlayerAfterConfirm,kickPlayer));
		sendChatMessage(fromPlayer,RESPONSE_WARNING,"要确认踢出，请输入指令：/rdi-confirm "+cmdId);
    }
	private void confirmKickPlayer(ServerPlayer fromPlayer,ServerPlayer kickPlayer){
		ThreadPool.newThread(()->{
			ResultData resultData = RdiHttpClient.sendRequest("delete", "/v37/island2/crew/" + kickPlayer.getStringUUID());
			if(resultData.isSuccess()){
				sendChatMessage(kickPlayer, RESPONSE_WARNING,fromPlayer.getScoreboardName() + "删除了他的岛屿!");
				resetProfile(kickPlayer);
				sendChatMessage(fromPlayer, RESPONSE_SUCCESS);
			}else sendServiceResultData(fromPlayer,resultData);

		});
	}
    private void invitePlayer(ServerPlayer player, ServerPlayer invitedPlayer) {
        if(player==invitedPlayer){
            sendChatMessage(player, RESPONSE_ERROR,"目标玩家不能是自己！");
            return;
        }
		ThreadPool.newThread(()->{
			ResultData resultData = RdiHttpClient.sendRequest("post", "/v37/island2/crew/" + player.getStringUUID() + "/" + invitedPlayer.getStringUUID());
			if(resultData.isSuccess()){
				sendChatMessage(player,"您邀请了"+invitedPlayer);
				sendChatMessage(invitedPlayer,RESPONSE_INFO,player.getScoreboardName()+"邀请您加入了他的岛屿");
				sendChatMessage(invitedPlayer,"按下[H键]可以前往他的岛屿");
				sendChatMessage(player,RESPONSE_SUCCESS,"1");
			}else sendServiceResultData(player,resultData);
		});

    }




}
