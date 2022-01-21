package calebzhou.rdimc.celestech.model.cache;

import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.model.IslandMember;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.JsonUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.List;

public class IslandCache implements ServerCache {
    public static final IslandCache instance = new IslandCache();
    private HashMap<String, Island> islandMap = new HashMap<>();//岛ID vs 岛
    private HashMap<String, String> ownIslandMap = new HashMap<>();//玩家ID  vs  岛ID
    private Multimap<String, String> memberMap = LinkedHashMultimap.create();//岛ID  vs  成员ID
    private int limit=512;

    @Override
    public void trim() {
        islandMap.clear();
    }

    @Override
    public boolean has(Object obj) {
        return islandMap.containsKey(obj);
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public boolean isFull() {
        return islandMap.size()>=limit;
    }

    public HashMap<String, Island> getIslandMap(){
        return islandMap;
    }

    public Multimap<String, String> getMemberMap() {
        return memberMap;
    }

    public HashMap<String, String> getOwnIslandMap() {
        return ownIslandMap;
    }

    public void loadCache(){
        ThreadPool.newThread(()->{
            String json  = HttpUtils.post("island","action=getall","query=SELECT @AST FROM Island");
            List<Island> island = JsonUtils.stringToArray(json, Island[].class);
            island.forEach(is-> {
                islandMap.put(is.getIslandId(),is);
                ownIslandMap.put(is.getOwnerUuid(),is.getIslandId());
            });
            json = HttpUtils.post("island","action=getall","query=SELECT @AST FROM IslandMember");
            List<IslandMember> members = JsonUtils.stringToArray(json, IslandMember[].class);
            members.forEach(is -> memberMap.put(is.getIslandId(),is.getMemberUuid()));

        });
    }
}
