package calebzhou.rdimc.celestech.module.structure;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.command.ArgCommand;
import calebzhou.rdimc.celestech.command.BaseCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.model.AreaSelection;
import calebzhou.rdimc.celestech.model.BorderedBox;
import calebzhou.rdimc.celestech.model.VirtualStructure;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VirtualStructureCommand extends BaseCommand implements ArgCommand {
    public VirtualStructureCommand(String name, int permissionLevel) {
        super(name, permissionLevel,false);
    }

    @Override
    public void onExecute(ServerPlayerEntity player, String nameArg) {
        int xp = 2;
        VirtualStructure.Type type = VirtualStructure.Type.valueOf(nameArg);
        VirtualStructure structure = new VirtualStructure(player.getEntityName(),type , BorderedBox.fromVec3i(AreaSelection.getPlayerSelectedArea(player.getUuidAsString())));
        switch (type){
            case clear -> {
                VirtualStructure.STRUCTURE_MAP.remove(player.getEntityName());
                try {
                    RDICeleTech.writeFiles();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                TextUtils.sendChatMessage(player,"成功移除了您设置过的所有结构区块~",MessageType.SUCCESS);
                return;
            }
            case swamp_hut -> {
                if(!structure.getRange().isSizeSmallerThan(7,7,9)){
                    TextUtils.sendChatMessage(player,"沼泽小屋的尺寸必须为7x7x9，当前为"+structure.getRange().getSizeStringX(),MessageType.ERROR);
                    return;
                }
                xp=50;
            }


        }
        PlayerUtils.checkExpLevel(player,xp);
        List<VirtualStructure> list = VirtualStructure.STRUCTURE_MAP.get(player.getEntityName());
        if(list==null)
            list=new ArrayList<>();
        list.add(structure);
        VirtualStructure.STRUCTURE_MAP.put(player.getEntityName(),list);
        TextUtils.sendChatMessage(player,"写入区块数据成功！",MessageType.SUCCESS);
        try {
            RDICeleTech.writeFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
