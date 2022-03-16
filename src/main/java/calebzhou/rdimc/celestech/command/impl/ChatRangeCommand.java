package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.ArgCommand;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import java.util.HashMap;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;

public class ChatRangeCommand extends BaseCommand implements ArgCommand {
    //玩家id vs 聊天对象玩家列表
    public static final HashMap<String, List<String>> chatRangeMap = new HashMap<>();
    public ChatRangeCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    public void onExecute(ServerPlayer player, String arg) {
        String pid = player.getStringUUID();
        char range = arg.charAt(0);
        if(range=='a'){
            //chatRangeMap.put(pid, ServerUtils.getOnlinePlayerNameList());
            chatRangeMap.remove(pid);
            TextUtils.sendChatMessage(player,"成功设定为[全服]聊天。您的聊天内容全服均可见。", MessageType.SUCCESS);
        }else if(range=='i'){
            ApiResponse response = HttpUtils.sendRequest("GET","v2/island/"+pid,"idType=pid");
            Island island = (Island) response.getData(Island.class);
            if(island==null){
                TextUtils.sendChatMessage(player,"您没有岛屿！",MessageType.ERROR);
                return;
            }
            List<String> members = island.getMembers();
            if(members==null || members.isEmpty()){
                TextUtils.sendChatMessage(player,"您的岛屿没有成员，因此不能使用此指令。",MessageType.ERROR);
                return;
            }
            chatRangeMap.put(pid,members);
            TextUtils.sendChatMessage(player,"成功设定为[岛内]聊天。您的聊天内容仅同岛好友可见。", MessageType.SUCCESS);
        }
    }
}
