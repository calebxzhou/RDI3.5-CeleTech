package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.ServiceConstants;
import calebzhou.rdimc.celestech.model.PlayerHome;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class ChangeIslandCommand extends BaseCommand {
    public ChangeIslandCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }
    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();

        ThreadPool.newThread(()->{
            HttpUtils.postObject("home",
                    PlayerHome.getIslandHomeLocation(player),
                    "action=LOCATE&homeName=island");
            TextUtils.sendChatMessage(player,"成功重新设定空岛位置。");
        });


        return 1;
    }

}
