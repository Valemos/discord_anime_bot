package bot.commands.arguments;

import bot.commands.CommandInfo;

public class CommandArgument extends ArgumentParser{

    private final CommandInfo commandInfo;

    public CommandArgument(CommandInfo commandInfo) {
        super("command \""+ commandInfo.name +'"');
        this.commandInfo = commandInfo;
    }

    @Override
    public ArgumentContent parse(String commandString, int startIndex) throws InvalidCommandException, ArgumentParseException {
        if (!stringIsCommand(commandString)){
            throw new InvalidCommandException(String.format("\"%s\" not a command", commandString));
        }

        String name = getCommandName(commandString);
        if (!isCommandNameValid(name)){
            throw new InvalidCommandException(String.format("\"%s\" invalid command", name));
        }

        return getNextArgumentContent(commandString, startIndex);
    }

    public static String getCommandName(String commandString) {
        if (!stringIsCommand(commandString)){
            return "";
        }

        try{
            int nameStart = commandString.indexOf(CommandInfo.commandChar) + 1;
            int nameEnd = commandString.indexOf(' ');
            if (nameEnd == -1) nameEnd = commandString.length();

            return commandString.substring(nameStart, nameEnd);
        }catch(IndexOutOfBoundsException e){
            return "";
        }
    }

    protected boolean isCommandNameValid(String command) {
        return command.equals(commandInfo.alias) ||
                command.equals(commandInfo.name);
    }

    public static boolean stringIsCommand(String string) {
        return string.startsWith(CommandInfo.commandChar);
    }

}
