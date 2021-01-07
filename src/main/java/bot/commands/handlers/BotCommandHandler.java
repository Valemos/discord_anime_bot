package bot.commands.handlers;

import bot.AccessLevel;
import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.DefaultMessageArguments;


public abstract class BotCommandHandler extends CommandWithArguments {

    protected CommandInfo commandInfo;
    protected AccessLevel accessLevel = AccessLevel.USER;

    public abstract void handleCommand(CommandParameters parameters);

    @Override
    public DefaultMessageArguments getArguments(String commandString) {
        DefaultMessageArguments arguments = new DefaultMessageArguments(commandInfo);
        return arguments.fromString(commandString);
    }

    public static boolean isNotCommandValid(BotCommandHandler handler) {
        return handler == null;
    }

    public String[] getCommandNames() {
        return commandInfo.getCommandNames();
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }
}
