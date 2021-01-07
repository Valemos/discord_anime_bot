package bot.commands.handlers;

import bot.commands.DefaultMessageArguments;
import bot.commands.MessageArguments;

public abstract class CommandWithArguments {
    public abstract MessageArguments getArguments(String command) throws DefaultMessageArguments.InvalidCommandPartsException;
}
