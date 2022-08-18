package calebzhou.rdimc.celestech.thread;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.player.Player;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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

    public RdiHttpPlayerRequest(Type type, Player player, int priority,  Consumer<String> doOnSuccess, String url,String... params) {
        super(type, url, priority, params);
        this.player = player;
        this.doOnSuccess = doOnSuccess;
    }
}
