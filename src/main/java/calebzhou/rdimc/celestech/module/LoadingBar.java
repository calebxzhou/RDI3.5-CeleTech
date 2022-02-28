package calebzhou.rdimc.celestech.module;

import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class LoadingBar extends Thread {

    public static void send(ServerPlayerEntity player, double timeElapseMs){
        new LoadingBar(player, timeElapseMs).start();
    }


    private final ServerPlayerEntity player;
    private final double timeElapseMs;

    public LoadingBar(ServerPlayerEntity player, double timeElapseMs) {
        this.player = player;
        this.timeElapseMs = timeElapseMs;
    }



    @Override
    public void run() {
        ServerBossBar bossBar = new ServerBossBar(new LiteralText("指令运行中"), BossBar.Color.BLUE, BossBar.Style.PROGRESS);
        bossBar.setPercent(0.0f);
        int percent = 100;
        player.networkHandler.sendPacket(BossBarS2CPacket.add(bossBar));
        for(int i=0;i<percent;++i){
            bossBar.setPercent((float) (0.015*i));
            try {
                Thread.sleep((long) (timeElapseMs/percent));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            player.networkHandler.sendPacket(BossBarS2CPacket.updateProgress(bossBar));
        }
        player.networkHandler.sendPacket(BossBarS2CPacket.remove(bossBar.getUuid()));
        bossBar.removePlayer(player);
        bossBar.setVisible(false);
        bossBar=null;
        //40个横杠
        /*int barAmount=40;
        String original = "执行中["+ ColorConstants.BRIGHT_GREEN+ "----------------------------------------]";
        for(int i=0;i<barAmount;++i){

            try {
                Thread.sleep((long) (timeElapseMs/barAmount));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            original = original.replaceFirst("-",">");
            TextUtils.sendActionMessage(player,original);
        }*/
    }
}
