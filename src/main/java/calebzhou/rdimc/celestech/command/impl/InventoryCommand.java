package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.utils.TextUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class InventoryCommand extends BaseCommand {
    public InventoryCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }
    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player =  source.getPlayer();
        NbtList nbtList = new NbtList();
        nbtList = player.getInventory().writeNbt(nbtList);
        TextUtils.sendChatMessage(source.getPlayer(),nbtList.asString());
        return 1;
    }
}
