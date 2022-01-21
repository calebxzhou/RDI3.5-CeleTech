package net.fabricmc;

import calebzhou.rdimc.celestech.constant.ChatRange;
import calebzhou.rdimc.celestech.model.ApiResponse;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.Island;
import calebzhou.rdimc.celestech.utils.HttpUtils;
import calebzhou.rdimc.celestech.utils.PlayerUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class EnumTest {
    public static void main(String[] args) {
        ApiResponse<Island> response = HttpUtils.sendRequest("POST", "island/123467853" );
        System.out.println(response);
        if (response.getType().equals("success")) {
            Island island = response.getData(Island.class);
            CoordLocation iloca = CoordLocation.fromString(island.getLocation());
            System.out.println(island);
            System.out.println(iloca);
        }
        System.out.println(response.getMessage());
    }
}
