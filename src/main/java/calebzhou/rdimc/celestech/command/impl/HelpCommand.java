package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.utils.TextUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public class HelpCommand extends RdiCommand {
    public HelpCommand( ) {
        super("rdi-help");
    }
    static final String help = """
            RDI帮助菜单
            /afklist挂机玩家列表 /dragon召唤末影龙 /encrypt设置密码 /home回岛
            /iplistip属地列表 /is岛屿系统 /SAVE存档 /spawn返回主城 /rti延迟tick
            /tpa传送 /tps服务器状态
            
            
            """;
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder.executes(context -> {TextUtils.sendChatMultilineMessage(context.getSource(),help) ;return 1;});
    }
}
