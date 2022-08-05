package calebzhou.rdimc.celestech.utils;

import net.minecraft.core.UUIDUtil;

public class UuidTest {
    public static void main(String[] args) {
        String wwtasdf = UUIDUtil.createOfflinePlayerUUID("Anotalotion").toString().replace("-","");
        System.out.println(wwtasdf);
    }
}
