package calebzhou.rdimc.celestech.command;

import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.function.PlayerLoadingBar;
import calebzhou.rdimc.celestech.module.island.IslandException;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.IntSummaryStatistics;

public abstract class BaseCommand {
    //指令执行时间map
    public static final Multimap<String,Integer> execTimeMap = LinkedHashMultimap.create();
    //指令英文名称
    private final String commandName;
    //是否异步执行
    private final boolean isAsync;


    public BaseCommand(String name, int permissionLevel,boolean isAsync) {
        this.commandName = name;
        this.builder = CommandManager.literal(name).requires(source -> source.hasPermissionLevel(permissionLevel));
        this.isAsync = isAsync;
    }

    protected LiteralArgumentBuilder<ServerCommandSource> builder;

    public LiteralArgumentBuilder<ServerCommandSource> getBuilder() {
        return builder;
    }

    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.executes(context -> execute(context.getSource(),new LiteralText(""))).then(CommandManager.argument("arg", MessageArgumentType.message())
                .executes(context -> execute(context.getSource(), MessageArgumentType.getMessage(context, "arg"))));
    }

    protected int execute(ServerCommandSource source, Text arg) throws CommandSyntaxException {
        long t1=System.currentTimeMillis();
        ServerPlayerEntity player = source.getPlayer();
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

    private void runCommand(Text arg, long t1, ServerPlayerEntity player) {
        IntSummaryStatistics summaryStats = execTimeMap.get(commandName).stream()
                .mapToInt((a) -> a)
                .summaryStatistics();
        double average = summaryStats.getAverage();
        PlayerLoadingBar.send(player,average);
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

    protected abstract void onExecute(ServerPlayerEntity player, String arg);
}
