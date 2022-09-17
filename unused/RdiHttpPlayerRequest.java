package calebzhou.rdi.core.server.thread;

import calebzhou.rdi.core.server.model.ResultData;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

//封装HTTP请求
public class RdiHttpPlayerRequest extends RdiHttpRequest{

    public Player player;
    public Consumer<ResultData> doOnSuccess;

    public RdiHttpPlayerRequest(Type type, Player player, Consumer<ResultData> doOnSuccess, String url, String... params) {
        super(type, url, params);
        this.player = player;
        this.doOnSuccess = doOnSuccess;
    }

}
