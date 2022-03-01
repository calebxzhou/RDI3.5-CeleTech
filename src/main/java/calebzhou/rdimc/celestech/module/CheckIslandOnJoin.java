package calebzhou.rdimc.celestech.module;

import calebzhou.rdimc.celestech.api.CallbackRegisterable;
import calebzhou.rdimc.celestech.event.PlayerConnectServerCallback;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.module.island.IslandException;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.IdentifierUtils;
import calebzhou.rdimc.celestech.utils.NetworkUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class CheckIslandOnJoin implements CallbackRegisterable {
    public CheckIslandOnJoin() {

    }
    private void checkHasIsland (ServerPlayerEntity player){
        ApiResponse<Island> response = null;
        boolean hasIsland = true;
        try {
            response = HttpUtils.sendRequestV2("GET","v2/island/"+player.getUuidAsString());
        } catch (NullPointerException| IslandException e) {
            hasIsland=false;
        }
        if(response==null || !response.isSuccess() || !hasIsland){
            NetworkUtils.sendPacketS2C(player,NetworkUtils.ISLAND_INFO,"404");
        }

    }

    @Override
    public void registerCallbacks() {
        PlayerConnectServerCallback.EVENT.register(IdentifierUtils.byClass(this.getClass()),((connection, player) -> {
            ThreadPool.newThread(()-> checkHasIsland(player));
            return ActionResult.PASS;
        }));
    }
}
