package bot.commands;

public abstract class MessageArguments {

    protected final CommandInfo commandInfo;

    public MessageArguments(CommandInfo commandInfo) {
        this.commandInfo = commandInfo;
    }

    public abstract boolean isValid();

    public abstract MessageArguments fromString(String commandString) throws MessageArguments.InvalidCommandPartsException;

    public static class InvalidCommandPartsException extends Exception {
        public InvalidCommandPartsException(String message) {
            super(message);
        }
    }
}
