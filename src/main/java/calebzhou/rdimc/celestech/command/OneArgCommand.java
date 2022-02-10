package calebzhou.rdimc.celestech.command;

import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.ThreadPool;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3i;

public abstract class OneArgCommand extends BaseCommand {
    private boolean isAsync = false;

    public OneArgCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }

    public OneArgCommand(String command, int permissionLevel, boolean isAsync) {
        super(command, permissionLevel);
        this.isAsync = isAsync;
    }

    @Override
    public LiteralArgumentBuilder<ServerCommandSource> setExecution() {
        return builder.then(CommandManager.argument("arg", MessageArgumentType.message())
                .executes(context -> execute(context.getSource(), MessageArgumentType.getMessage(context, "arg"))));
    }

    protected int execute(ServerCommandSource source, Text arg) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        String args = arg.getString();
        try {
            if (isAsync)
                ThreadPool.newThread(() -> {
                    try {
                        onExecute(player, args);
                    } catch (NumberFormatException e) {
                        TextUtils.sendChatMessage(player, "数字格式错误", MessageType.ERROR);
                    } catch (IndexOutOfBoundsException e) {
                        TextUtils.sendChatMessage(player, "参数数量错误", MessageType.ERROR);
                    } catch (IllegalArgumentException e) {
                        TextUtils.sendChatMessage(player, "参数类型错误", MessageType.ERROR);
                    } catch (NullPointerException e) {
                        TextUtils.sendChatMessage(player, "目标不能为空！", MessageType.ERROR);
                    } catch (Exception e) {
                        e.printStackTrace();
                        TextUtils.sendChatMessage(player, e.getMessage(), MessageType.ERROR);
                    }
                });
            else
                onExecute(player, args);
        } catch (NumberFormatException e) {
            TextUtils.sendChatMessage(player, "数字格式错误", MessageType.ERROR);
        } catch (IndexOutOfBoundsException e) {
            TextUtils.sendChatMessage(player, "参数数量错误", MessageType.ERROR);
        } catch (IllegalArgumentException e) {
            TextUtils.sendChatMessage(player, "参数类型错误", MessageType.ERROR);
        } catch (NullPointerException e) {
            TextUtils.sendChatMessage(player, "目标不能为空！", MessageType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            TextUtils.sendChatMessage(player, e.getMessage(), MessageType.ERROR);
        }

        return Command.SINGLE_SUCCESS;
    }

    //适用于x1,y1,z1,x2,y2,z2这样参数的指令，分割成两个向量
    protected Vec3i[] parseToPosition(String arg) throws ArrayIndexOutOfBoundsException {
        String[] split = arg.split(",");
        Vec3i xyz1 = new Vec3i(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        Vec3i xyz2 = new Vec3i(Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]));
        return new Vec3i[]{xyz1, xyz2};
    }

    protected abstract void onExecute(ServerPlayerEntity player, String arg);
}
