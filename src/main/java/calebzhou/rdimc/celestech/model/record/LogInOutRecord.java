package calebzhou.rdimc.celestech.model.record;

import calebzhou.rdimc.celestech.constant.LogAction;
import calebzhou.rdimc.celestech.utils.TimeUtils;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.Serializable;
import java.sql.Timestamp;

public class LogInOutRecord implements Serializable {
    String playerUuid;
    String logAction;//IN OUT
    String ipAddr;
    Timestamp oprTime;

    public LogInOutRecord(String playerUuid, String inout, String ipAddr,Timestamp oprTime) {
        this.playerUuid = playerUuid;
        this.logAction = inout;
        this.ipAddr = ipAddr;
        this.oprTime = oprTime;
    }
    public LogInOutRecord(ServerPlayerEntity player, LogAction action){
        this.playerUuid = player.getUuidAsString();
        this.logAction=action.toString();
        this.ipAddr = player.getIp();
        this.oprTime = TimeUtils.getNow();
    }
}
