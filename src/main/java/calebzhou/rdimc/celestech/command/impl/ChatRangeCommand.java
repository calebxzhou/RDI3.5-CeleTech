package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.OneArgCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.List;

public class ChatRangeCommand extends OneArgCommand {
    //玩家id vs 聊天对象玩家列表
    public static final HashMap<String, List<String>> chatRangeMap = new HashMap<>();
    public ChatRangeCommand(String name, int permissionLevel) {
        super(name, permissionLevel,true);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player, String arg) {
        String pid = player.getUuidAsString();
        char range = arg.charAt(0);
        if(range=='a'){
            //chatRangeMap.put(pid, ServerUtils.getOnlinePlayerNameList());
            chatRangeMap.remove(pid);
            TextUtils.sendChatMessage(player,"成功设定为[全服]聊天。", MessageType.SUCCESS);
        }else if(range=='i'){
            ApiResponse response = HttpUtils.sendRequest("GET","island/"+pid);
            Island island = (Island) response.getData(Island.class);
            List<String> members = island.getMembers();
            if(members==null || members.isEmpty()){
                TextUtils.sendChatMessage(player,"您的空岛没有成员！",MessageType.ERROR);
                return;
            }
            chatRangeMap.put(pid,members);
            TextUtils.sendChatMessage(player,"成功设定为[岛内]聊天。", MessageType.SUCCESS);
        }
    }
}
