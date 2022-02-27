package calebzhou.rdimc.celestech.module.cloudrepo;

import calebzhou.rdimc.celestech.command.ArgCommand;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.JsonUtils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;

public class CloudRepoCommand extends BaseCommand implements ArgCommand {
    public CloudRepoCommand(String name, int permissionLevel) {
        super(name, permissionLevel, true);
    }

    @Override
    public void onExecute(ServerPlayerEntity player, String arg) {
        CloudRepoAction action = CloudRepoAction.valueOf(arg);
        switch (action){
            case read -> {

            }
            case save -> save(player);
        }
    }
    private void save(ServerPlayerEntity player){
        PlayerInventory inventory = player.getInventory();
        ArrayList<String> itemStacks = new ArrayList<>();
        for (int i = 0; i <=35 ; i++) {
            ItemStack stack = inventory.getStack(i);
            NbtCompound stackNbt = stack.writeNbt(new NbtCompound());
            itemStacks.add(stackNbt.toString());
        }
        String json = JsonUtils.getGson().toJson(itemStacks);
        HttpUtils.sendRequestV2("POST","cloudrepo/"+player.getUuidAsString(),"obj="+json);
    }
}
