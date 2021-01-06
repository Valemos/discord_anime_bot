package bot.commands;

public abstract class CommandWithArguments {
    public abstract MessageArguments getArguments(String command);
}
