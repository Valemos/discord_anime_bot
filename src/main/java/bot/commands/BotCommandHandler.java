package bot.commands;

import bot.AccessLevel;

import java.util.Arrays;


public abstract class BotCommandHandler extends CommandParser {

    protected String commandName = "";

    protected AccessLevel accessLevel = AccessLevel.USER;

    public abstract void handleCommand(CommandArguments arguments);

    @Override
    public String[] getArguments(String command) {
        String[] parts = getStringCommandParts(command);

        if (commandPartsValid(parts)){
            return Arrays.copyOfRange(parts, 1, parts.length);
        }
        return null;
    }

    @Override
    public boolean isArgumentsValid(String[] arguments) {
        return true;
    }

    protected boolean commandPartsValid(String[] parts){
        if (parts != null) {
            if (parts.length > 0) {
                return parts[0].equals(commandPrefix + commandName);
            }
        }
        return false;
    }

    public String getName() {
        return commandName;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }
}
