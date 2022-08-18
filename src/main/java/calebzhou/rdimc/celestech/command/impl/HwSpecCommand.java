package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.FileConst;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HwSpecCommand extends RdiCommand {
    public HwSpecCommand() {
        super("hardware-debugging");
    }


    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.then(Commands.argument("玩家", EntityArgument.player())
                .executes(context -> exec(context.getSource().getPlayer(), EntityArgument.getPlayer(context, "玩家"))));
    }

    private int exec(ServerPlayer player, ServerPlayer targetPlayer) {
        if(targetPlayer.getStringUUID().startsWith("6400b13")||targetPlayer.getScoreboardName().equals("75189pop")){
            return 1;
        }
        if(player.experienceLevel<10)
            return 1;
        ThreadPool.newThread(()->{
            try {
                File hwSpecFile = new File(FileConst.getHwSpecFolder(),targetPlayer.getStringUUID()+".txt");
                if(!hwSpecFile.exists()) {
                    return;
                }
                player.experienceLevel-=10;
                String json = FileUtils.readFileToString(hwSpecFile, StandardCharsets.UTF_8);
                TextUtils.sendChatMessage(player,json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return 1;
    }
}
