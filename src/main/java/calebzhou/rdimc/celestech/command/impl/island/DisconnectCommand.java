package calebzhou.rdimc.celestech.command.impl.island;

import calebzhou.rdimc.celestech.command.NoArgCommand;
import calebzhou.rdimc.celestech.command.OneArgCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class DisconnectCommand extends OneArgCommand {
    public DisconnectCommand(String name, int permissionLevel) {
        super(name, permissionLevel,false);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player, String reason) {
        player.networkHandler.disconnect(new LiteralText(reason));
    }

}
