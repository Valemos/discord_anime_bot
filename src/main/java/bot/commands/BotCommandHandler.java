package bot.commands;

import bot.AccessLevel;


public abstract class BotCommandHandler extends CommandWithArguments {

    protected CommandInfo commandInfo;
    protected AccessLevel accessLevel = AccessLevel.USER;

    public abstract void handleCommand(CommandParameters args);

    @Override
    public DefaultMessageArguments getArguments(String commandString) {
        DefaultMessageArguments arguments = new DefaultMessageArguments(commandInfo);
        return arguments.fromString(commandString);
    }

    public static boolean commandIsInvalid(BotCommandHandler handler) {
        return handler == null;
    }

    public String[] getCommandNames() {
        return commandInfo.getCommandNames();
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

}
