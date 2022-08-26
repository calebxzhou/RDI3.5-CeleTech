package calebzhou.rdimc.celestech.thread;

import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

//封装HTTP请求
public class RdiHttpPlayerRequest extends RdiHttpRequest{

    public Player player;
    public Consumer<String> doOnSuccess;

    public RdiHttpPlayerRequest(Type type, Player player, Consumer<String> doOnSuccess, String url, String... params) {
        super(type, url, params);
        this.player = player;
        this.doOnSuccess = doOnSuccess;
    }

}
