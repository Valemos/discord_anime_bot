package bot.commands.handlers;

import bot.commands.CommandInfo;
import bot.commands.CommandParser;

public class EmptyMessageArguments extends DefaultMessageArguments{

    public EmptyMessageArguments(CommandInfo commandInfo) {
        super(commandInfo);
    }

    @Override
    public boolean isValid() {

        return true;
    }

    @Override
    public EmptyMessageArguments fromString(String commandString) throws InvalidCommandException {
        super.fromString(commandString);
        return this;
    }
}
