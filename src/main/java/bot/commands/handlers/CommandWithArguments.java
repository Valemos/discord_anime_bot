package bot.commands.handlers;

public abstract class CommandWithArguments {
    public abstract MessageArguments getArguments(String command) throws MessageArguments.InvalidCommandException;
}
