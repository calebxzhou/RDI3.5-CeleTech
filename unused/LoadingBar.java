package calebzhou.rdimc.celestech.module;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;

public class LoadingBar extends Thread {

    public static void send(ServerPlayer player, double timeElapseMs){
        new LoadingBar(player, timeElapseMs).start();
    }


    private final ServerPlayer player;
    private final double timeElapseMs;

    public LoadingBar(ServerPlayer player, double timeElapseMs) {
        this.player = player;
        this.timeElapseMs = timeElapseMs;
    }



    @Override
    public void run() {
        ServerBossEvent bossBar = new ServerBossEvent(new TextComponent("指令运行中"), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);
        bossBar.setProgress(0.0f);
        int percent = 100;
        player.connection.send(ClientboundBossEventPacket.createAddPacket(bossBar));
        for(int i=0;i<percent;++i){
            bossBar.setProgress((float) (0.015*i));
            try {
                Thread.sleep((long) (timeElapseMs/percent));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            player.connection.send(ClientboundBossEventPacket.createUpdateProgressPacket(bossBar));
        }
        player.connection.send(ClientboundBossEventPacket.createRemovePacket(bossBar.getId()));
        bossBar.removePlayer(player);
        bossBar.setVisible(false);
        bossBar=null;

    }
}
