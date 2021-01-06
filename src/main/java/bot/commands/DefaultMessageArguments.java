package bot.commands;

import java.util.Arrays;

public class DefaultMessageArguments extends MessageArguments {

    public String[] commandParts = new String[]{};

    public DefaultMessageArguments(CommandInfo commandInfo) {
        super(commandInfo);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public DefaultMessageArguments fromString(String commandString) {
        String[] parts = CommandParser.getStringCommandParts(commandString);

        if (commandPartsValid(parts)) {
            if (parts.length == 1) {
                commandParts = new String[]{};
            } else {
                commandParts = Arrays.copyOfRange(parts, 1, parts.length);
            }
            return this;
        }else{
            return null;
        }
    }

    protected boolean commandPartsValid(String[] parts){
        if (parts != null) {
            if (parts.length > 0) {
                return parts[0].equals(CommandInfo.commandChar + commandInfo.name);
            }
        }
        return false;
    }
}
