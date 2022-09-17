package calebzhou.rdi.celestech.module;

import calebzhou.rdi.celestech.api.CallbackRegisterable;
import calebzhou.rdi.celestech.event.PlayerConnectServerCallback;
import calebzhou.rdi.celestech.model.ApiResponse;
import calebzhou.rdi.celestech.model.Island;
import calebzhou.rdi.celestech.module.island.IslandException;
import calebzhou.rdi.celestech.utils.HttpUtils;
import calebzhou.rdi.celestech.utils.IdentifierUtils;
import calebzhou.rdi.celestech.utils.NetworkUtils;
import calebzhou.rdi.celestech.utils.ThreadPool;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

public class CheckIslandOnJoin implements CallbackRegisterable {
    public CheckIslandOnJoin() {

    }
    private void checkHasIsland (ServerPlayer player){
        ApiResponse<Island> response = null;
        boolean hasIsland = true;
        try {
            response = HttpUtils.sendRequestV2("GET","v2/island/"+player.getStringUUID());
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
            return InteractionResult.PASS;
        }));
    }
}
