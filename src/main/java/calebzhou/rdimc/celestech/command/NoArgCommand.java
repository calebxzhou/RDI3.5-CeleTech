package calebzhou.rdimc.celestech.command;

import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class NoArgCommand extends BaseCommand{
    private boolean isAsync = false;

    public NoArgCommand(String name, int permissionLevel, boolean isAsync) {
        super(name, permissionLevel);
        this.isAsync = isAsync;
    }

    public NoArgCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }

    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        try {
        if(isAsync)
            ThreadPool.newThread(()->onExecute(player));
        else
            onExecute(player);


        }catch (NumberFormatException  e){
            TextUtils.sendChatMessage(player,"数字格式错误", MessageType.ERROR);
        }catch (IndexOutOfBoundsException e){
            TextUtils.sendChatMessage(player,"参数数量错误",MessageType.ERROR);
        }catch (IllegalArgumentException e){
            TextUtils.sendChatMessage(player,"参数类型错误",MessageType.ERROR);
        }catch (NullPointerException e){
            TextUtils.sendChatMessage(player,"目标不能为空！",MessageType.ERROR);
        }catch (Exception e) {
            e.printStackTrace();
            TextUtils.sendChatMessage(player,e.getMessage(),MessageType.ERROR);
        }
        return Command.SINGLE_SUCCESS;
    }

    protected abstract void onExecute(ServerPlayerEntity player);
}
