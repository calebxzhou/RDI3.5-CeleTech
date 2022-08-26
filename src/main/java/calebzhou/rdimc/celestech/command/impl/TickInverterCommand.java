package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.module.tickinv.BlockEntityTickInverter;
import calebzhou.rdimc.celestech.module.tickinv.EntityTickInverter;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.server.level.ServerPlayer;

public class TickInverterCommand extends RdiCommand {
    public TickInverterCommand() {
        super("rti");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder
                .executes(this::exec)
                .then(Commands.argument("arg", StringArgumentType.string()).suggests( (context, builder) ->  SharedSuggestionProvider.suggest(new String[]{"intro","entity","block-entity"},builder))
                        .executes(this::exec2));
    }



    static final String help = """
            ---指令参数---
            intro 介绍本系统
            entity 仍未运算的实体
            block-entity 仍未运算的容器
            """;
    static final String intro = """
            ===RDI TickInverter 简介===
            众所周知，Minecraft处理逻辑的次数是1秒20次，每次处理逻辑的时间为50毫秒左右，这50毫秒通常被称为1tick（刻）。也就是说，正常的游戏逻辑的处理速度是20tick每秒。
            但是事实往往事与愿违，当客户端之间有大量的数据需要交换处理时，所有客户端的逻辑处理速度都无法在50毫秒内处理完成一个1tick，这样就会导致游戏逻辑卡顿，
            从而无法实现容器的开启关闭， 实体的攻击等操作。
            为了解决这一现象，RDI TickInverter (RTI)应运而生了。RTI会检测所有客户端处理逻辑的时间是否在50毫秒内，
            一旦超过50毫秒，则会把多出来的、无法来得及处理的tick任务存进一个队列中。等到延迟降低到50毫秒以下时，再将
            tick任务依次取出并执行，从而保证游戏的顺利运行。
            """;
    private int exec(CommandContext<CommandSourceStack> cox) {
        CommandSourceStack source = cox.getSource();
        TextUtils.sendChatMessage(source,help);
        return 0;
    }
    private int exec2(CommandContext<CommandSourceStack> context) {
        String arg = StringArgumentType.getString(context, "arg");
        CommandSourceStack source = context.getSource();
        switch (arg){
            case "intro" -> TextUtils.sendChatMessage(source,intro);
            case "entity" -> execEntities(source);
            case "block-entity" -> execBlockEntites(source);
        }
        return 1;
    }

    private void execBlockEntites(CommandSourceStack source) {
        ThreadPool.newThread(()->{
            BlockEntityTickInverter instance = BlockEntityTickInverter.INSTANCE;
            TextUtils.sendChatMessage(source, "当前全服共有%d个容器进入了延迟tick列表".formatted(instance.getDelayTickListSize()));
            ServerPlayer player = source.getPlayer();
            if(player==null)
                return;
            //TODO 查看附近的被延迟的tick
        });


    }

    private void execEntities(CommandSourceStack source) {
        ThreadPool.newThread(()->{
            EntityTickInverter instance = EntityTickInverter.INSTANCE;
            TextUtils.sendChatMessage(source, "当前全服共有%d个实体进入了延迟tick列表".formatted(instance.getDelayTickListSize()));
            ServerPlayer player = source.getPlayer();
            if(player==null)
                return;
            //TODO 查看附近的被延迟的tick

        });
    }
}
