package calebzhou.rdimc.celestech.module.cloudrepo;

import calebzhou.rdimc.celestech.command.ArgCommand;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.JsonUtils;
import java.util.ArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class CloudRepoCommand extends BaseCommand implements ArgCommand {
    public CloudRepoCommand(String name, int permissionLevel) {
        super(name, permissionLevel, true);
    }

    @Override
    public void onExecute(ServerPlayer player, String arg) {
        CloudRepoAction action = CloudRepoAction.valueOf(arg);
        switch (action){
            case read -> {

            }
            case save -> save(player);
        }
    }
    private void save(ServerPlayer player){
        Inventory inventory = player.getInventory();
        ArrayList<String> itemStacks = new ArrayList<>();
        for (int i = 0; i <=35 ; i++) {
            ItemStack stack = inventory.getItem(i);
            CompoundTag stackNbt = stack.save(new CompoundTag());
            itemStacks.add(stackNbt.toString());
        }
        String json = JsonUtils.getGson().toJson(itemStacks);
        HttpUtils.sendRequestV2("POST","cloudrepo/"+player.getStringUUID(),"obj="+json);
    }
}
