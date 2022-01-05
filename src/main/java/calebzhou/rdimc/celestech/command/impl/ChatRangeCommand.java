package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.ChatAction;
import calebzhou.rdimc.celestech.constant.ChatRange;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.cache.CharRangeCache;
import calebzhou.rdimc.celestech.model.cache.ChatRecordCache;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;

import static calebzhou.rdimc.celestech.utils.TextUtils.*;
public class ChatRangeCommand extends BaseCommand {
    public ChatRangeCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }
    public static final ArrayList<String> rangeList =new ArrayList<>();
    static{
        rangeList.add("all");
        rangeList.add("team");
        rangeList.add("mute");
    }
    public static final ArrayList<String> actionList =new ArrayList<>();
    static{
        actionList.add("say");
        actionList.add("receive");
    }
    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.then(CommandManager.argument("action",StringArgumentType.string())
        ).then(CommandManager.argument("range", StringArgumentType.string()).executes(
                context -> execute( context.getSource(), StringArgumentType.getString(context,"action"), StringArgumentType.getString(context,"range"))
        ));
    }

    private int execute(ServerCommandSource source,String action,String range) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        if(!actionList.contains(action)){
            sendChatMessage(player,"您输入的聊天操作有误!正确的操作是: say 说话, receive 接收", MessageType.ERROR);
            return 1;
        }
        if(!rangeList.contains(range)){
            sendChatMessage(player,"您输入的聊天范围有误!正确的范围是: all 所有,team 岛内, mute 静音",MessageType.ERROR);
            return 1;
        }
        CharRangeCache.instance.getTable().put(player.getEntityName(), ChatAction.valueOf(action), ChatRange.valueOf(range));
        sendChatMessage(player,"您已成功设定聊天范围!",MessageType.SUCCESS);
        return 1;
    }
}
