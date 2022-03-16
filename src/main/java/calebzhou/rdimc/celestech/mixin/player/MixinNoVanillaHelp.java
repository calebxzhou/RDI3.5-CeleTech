package calebzhou.rdimc.celestech.mixin.player;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.HelpCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(HelpCommand.class)
public class MixinNoVanillaHelp {
    /**
     * @author 不显示帮助菜单
     */
    @Overwrite
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

    }
}
