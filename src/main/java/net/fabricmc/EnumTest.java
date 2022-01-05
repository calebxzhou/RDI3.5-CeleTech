package net.fabricmc;

import calebzhou.rdimc.celestech.constant.ChatRange;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumTest {
    public static void main(String[] args) {
        System.out.println(Arrays.stream(ChatRange.values()).collect(Collectors.toList()));
    }
}
