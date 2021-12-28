package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.ServiceConstants;
import calebzhou.rdimc.celestech.model.PlayerHome;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.google.gson.Gson;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class DeleteIslandCommand extends BaseCommand {
    public DeleteIslandCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }
    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();

        ThreadPool.newThread(()->{
            String homeResp = HttpUtils.doGet(ServiceConstants.ADDR+
                    String.format("home?action=DELETE&playerUuid=%s&homeName=island", player.getUuidAsString()));
            TextUtils.sendChatMessage(player,homeResp);
            player.getInventory().clear();
            player.kill();
        });


        return 1;
    }

}
