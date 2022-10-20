package calebzhou.rdi.core.server.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

/**
 * Created by calebzhou on 2022-10-07,7:50.
 */
abstract class RdiNormalCommand:RdiCommand {
    final override val commandName : String
    val description:String
    val displayInHelp:Boolean
    protected val baseArgBuilder: LiteralArgumentBuilder<CommandSourceStack>

    constructor(commandName:String){
        this.commandName=commandName
        this.description=""
        this.displayInHelp=false
        this.baseArgBuilder = Commands.literal(commandName)
    }
    constructor(commandName:String,description:String){
        this.commandName=commandName
        this.description=description
        this.displayInHelp=false
        this.baseArgBuilder = Commands.literal(commandName)
    }
    constructor(commandName:String,description:String,displayInHelp:Boolean){
        this.commandName=commandName
        this.description=description
        this.displayInHelp=displayInHelp
        this.baseArgBuilder = Commands.literal(commandName)
    }

}
