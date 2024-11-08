package calebzhou.rdimc.celestech.command.impl.island;

import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.constant.WorldConst;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class DeleteCommand extends RdiCommand {

    @Override
    public String getName() {
        return "island-delete";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> exec(context.getSource().getPlayer()));
    }

    private int exec(ServerPlayer player) {
        ThreadPool.newThread(() -> {
            String resp = HttpUtils.sendRequest("delete", "island/" + player.getStringUUID());
            if (resp.equals("1")) {
                player.getInventory().clearContent();
                player.kill();
                PlayerUtils.teleport(player, WorldConst.SPAWN_LOCA);
                player.setRespawnPosition(Level.OVERWORLD, new BlockPos(WorldConst.SPAWN_LOCA.x, WorldConst.SPAWN_LOCA.y, WorldConst.SPAWN_LOCA.z), 0, true, false);
                sendChatMessage(player, MessageType.SUCCESS, "1");
            } else {
                sendChatMessage(player, MessageType.ERROR, "您未拥有空岛！");
            }
        });

        return 1;
    }

}
