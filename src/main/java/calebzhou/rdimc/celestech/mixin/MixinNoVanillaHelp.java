package calebzhou.rdimc.celestech.mixin;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.HelpCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(HelpCommand.class)
public class MixinNoVanillaHelp {
    /**
     * @author 不显示帮助菜单
     */
    @Overwrite
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

    }
}
