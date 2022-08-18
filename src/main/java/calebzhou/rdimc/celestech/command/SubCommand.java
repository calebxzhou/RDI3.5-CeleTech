package calebzhou.rdimc.celestech.command;

import java.util.function.Function;

public class SubCommand {
    public String command;
    public String description;
    public Function<?,?> functionToDo;
}
