package calebzhou.rdimc.celestech.event.impl;

import calebzhou.rdimc.celestech.constant.ColorConstants;
import calebzhou.rdimc.celestech.event.PlayerChatCallback;
import calebzhou.rdimc.celestech.event.PlayerDeathCallback;
import calebzhou.rdimc.celestech.model.PlayerTemperature;
import calebzhou.rdimc.celestech.model.record.GenericRecord;
import calebzhou.rdimc.celestech.model.record.RecordType;
import calebzhou.rdimc.celestech.utils.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.apache.commons.lang3.RandomUtils;

import java.util.HashSet;

//与服务器交互事件
public class PlayerMiscEvent {

    public PlayerMiscEvent() {
        //死亡事件
        PlayerDeathCallback.EVENT.register(((player, source) -> {
            HttpUtils.postObject(new GenericRecord(player.getUuidAsString(), RecordType.death, source.toString(), null,null));
            //还原体温
            //PlayerTemperature.put(player.getEntityName(),PlayerTemperature.DEFAULT_TEMP);
            //随机掉落

            return ActionResult.PASS;
        }));


    }

    public void randomDropHandler(PlayerEntity player){

    }
}
