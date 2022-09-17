package calebzhou.rdi.core.server.command.impl;


import calebzhou.rdi.core.server.command.RdiCommand;
import calebzhou.rdi.core.server.constant.FileConst;
import calebzhou.rdi.core.server.utils.ThreadPool;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static calebzhou.rdi.core.server.utils.PlayerUtils.*;

public class EncryptCommand extends RdiCommand {

    public EncryptCommand( ) {
        super( "encrypt");
    }


    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder
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
            sendChatMessage(player, RESPONSE_ERROR,"两次密码输入不一致！");
            return 1;
        }
        if(pwdVerify.length()<6||pwdVerify.length()>16){
            sendChatMessage(player, RESPONSE_ERROR,"密码最少6位，最多16位！");
            return 1;
        }
        ThreadPool.newThread(()->{
            try {
            File pwdFile = new File(FileConst.getPasswordFolder(),player.getStringUUID()+".txt");
            if(!pwdFile.exists()) {
                sendChatMessage(player, RESPONSE_INFO,"正在加密账号数据....");
                pwdFile.createNewFile();
            }
            FileUtils.write(pwdFile,pwdVerify, StandardCharsets.UTF_8);
                sendChatMessage(player, RESPONSE_SUCCESS,"加密成功，请牢记您的密码："+pwdVerify);
                sendChatMessage(player, RESPONSE_SUCCESS,"切勿随意将 崩溃报告、游戏日志、启动器配置 等信息发送给不信任的人，其中会包含您的密码！");
                sendChatMessage(player, RESPONSE_SUCCESS,"客户端解密配置教程，请查看群文件pdf。");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return 1;
    }
}
