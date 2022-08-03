package calebzhou.rdimc.celestech.command;

import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.StringUtils;

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

    public abstract LiteralArgumentBuilder<CommandSourceStack> setExecution() ;
    /*

        protected abstract int execute(CommandSourceStack source, ServerPlayer arg) throws CommandSyntaxException {
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
    */
    private void runCommand(Component arg, long t1, ServerPlayer player) {

        try {
            if(BaseCommand.this instanceof ArgCommand)
                if(StringUtils.isEmpty(arg.getString())){
                    TextUtils.sendChatMessage(player,"指令参数不可为空！");
                    return;
                }
            onExecute(player, arg.getString());
        }catch (NullPointerException e){
            TextUtils.sendChatMessage(player, MessageType.ERROR,"找不到指令的目标！");
        }
        catch (Exception e) {
            e.printStackTrace();
            TextUtils.sendChatMessage(player, MessageType.ERROR, e.getMessage());
        }finally {
            long t2=System.currentTimeMillis();
            int deltaT = (int) (t2-t1);
            execTimeMap.put(commandName,deltaT);
        }
    }
}
