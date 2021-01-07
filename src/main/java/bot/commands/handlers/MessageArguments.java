package bot.commands.handlers;

import bot.commands.CommandInfo;
import bot.commands.CommandParser;

public abstract class MessageArguments {

    protected final CommandInfo commandInfo;

    public MessageArguments(CommandInfo commandInfo) {
        this.commandInfo = commandInfo;
    }

    public abstract boolean isValid();

    public abstract MessageArguments fromString(String commandString) throws InvalidCommandException;

    public static class InvalidCommandException extends Exception {
        public InvalidCommandException(String message) {
            super(message);
        }
    }
}
