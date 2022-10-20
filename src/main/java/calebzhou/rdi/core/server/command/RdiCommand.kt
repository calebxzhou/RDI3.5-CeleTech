package calebzhou.rdi.core.server.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.CommandSourceStack

/**
 * Created  on 2022-10-18,8:42.
 */
interface RdiCommand {
    val commandName:String
    val execution: LiteralArgumentBuilder<CommandSourceStack>
}
