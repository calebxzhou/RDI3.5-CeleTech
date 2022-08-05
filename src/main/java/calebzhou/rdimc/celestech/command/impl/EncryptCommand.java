package calebzhou.rdimc.celestech.command.impl;


import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.FileConst;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EncryptCommand implements RdiCommand {

    @Override
    public String getName() {
        return "encrypt";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return Commands.literal(getName())
                .then(Commands.argument("用来加密的密码", StringArgumentType.string())
                        .then(Commands.argument("确认要加密的密码",StringArgumentType.string())
                                .executes(context -> exec(context.getSource().getPlayer(),
                                        StringArgumentType.getString(context,"用来加密的密码"),
                                        StringArgumentType.getString(context,"确认要加密的密码")

                                        )
                                )
                        )
                );
    }

    private int exec(ServerPlayer player, String pwdSet, String pwdVerify) {
        if(!pwdSet.equals(pwdVerify)){
            TextUtils.sendChatMessage(player, MessageType.ERROR,"两次密码输入不一致！");
            return 1;
        }
        if(pwdVerify.length()<6||pwdVerify.length()>16){
            TextUtils.sendChatMessage(player, MessageType.ERROR,"密码最少6位，最多16位！");
            return 1;
        }
        ThreadPool.newThread(()->{
            try {
            File pwdFile = new File(FileConst.PASSWORD_FOLDER,player.getStringUUID()+".txt");
            if(!pwdFile.exists()) {
                TextUtils.sendChatMessage(player, MessageType.INFO,"正在加密账号数据....");
                pwdFile.createNewFile();
            }
            FileUtils.write(pwdFile,pwdVerify, StandardCharsets.UTF_8);
                TextUtils.sendChatMessage(player, MessageType.SUCCESS,"加密成功，请牢记您的密码："+pwdVerify);
                TextUtils.sendChatMessage(player, MessageType.SUCCESS,"切勿随意将 崩溃报告、游戏日志、启动器配置 等信息发送给不信任的人，其中会包含您的密码！");
                TextUtils.sendChatMessage(player, MessageType.SUCCESS,"客户端解密配置教程，请查看群文件pdf。");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return 1;
    }
}
