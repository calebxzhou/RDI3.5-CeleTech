package calebzhou.rdimc.celestech.command;

import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.module.LoadingBar;
import calebzhou.rdimc.celestech.module.island.IslandException;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.IntSummaryStatistics;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public abstract class BaseCommand {
    //指令执行时间map
    public static final Multimap<String,Integer> execTimeMap = LinkedHashMultimap.create();
    //指令英文名称
    private final String commandName;
    //是否异步执行
    private final boolean isAsync;


    public BaseCommand(String name, int permissionLevel,boolean isAsync) {
        this.commandName = name;
        this.builder = Commands.literal(name).requires(source -> source.hasPermission(permissionLevel));
        this.isAsync = isAsync;
    }

    protected LiteralArgumentBuilder<CommandSourceStack> builder;

    public LiteralArgumentBuilder<CommandSourceStack> getBuilder() {
        return builder;
    }

    public LiteralArgumentBuilder<CommandSourceStack> setExecution() {
        return builder.executes(context -> execute(context.getSource(),new TextComponent(""))).then(Commands.argument("arg", MessageArgument.message())
                .executes(context -> execute(context.getSource(), MessageArgument.getMessage(context, "arg"))));
    }

    protected int execute(CommandSourceStack source, Component arg) throws CommandSyntaxException {
        long t1=System.currentTimeMillis();
        ServerPlayer player = source.getPlayerOrException();
        if(execTimeMap.size()>1024)
            execTimeMap.clear();
        if(isAsync){
            ThreadPool.newThread(()->{
                runCommand(arg, t1, player);

            });
        } else{
            runCommand(arg, t1, player);
        }

        return 1;
    }

    private void runCommand(Component arg, long t1, ServerPlayer player) {

        try {
            if(BaseCommand.this instanceof ArgCommand)
                if(StringUtils.isEmpty(arg.getString()))
                    throw new IslandException("指令参数不可为空！");
            onExecute(player, arg.getString());
        }catch (IslandException|AreaException|ExperienceException e){
            TextUtils.sendChatMessage(player,e.getMessage(), MessageType.ERROR);
                } catch (NumberFormatException e) {
                    TextUtils.sendChatMessage(player, "数字格式错误", MessageType.ERROR);
                /*} catch (IndexOutOfBoundsException e) {
                    TextUtils.sendChatMessage(player, "参数数量错误", MessageType.ERROR);
                } catch (IllegalArgumentException e) {
                    TextUtils.sendChatMessage(player, "参数类型错误", MessageType.ERROR);
                } catch (NullPointerException e) {
                    TextUtils.sendChatMessage(player, "目标不能为空！", MessageType.ERROR);*/
        } catch (Exception e) {
            e.printStackTrace();
            TextUtils.sendChatMessage(player, e.getMessage(), MessageType.ERROR);
        }finally {

            long t2=System.currentTimeMillis();
            int deltaT = (int) (t2-t1);
            execTimeMap.put(commandName,deltaT);
        }
    }

    protected abstract void onExecute(ServerPlayer player, String arg);
}
