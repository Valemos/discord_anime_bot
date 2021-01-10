package bot.commands.handlers;

import bot.PlayerAccessLevel;
import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.parsing.ArgumentSettings;
import bot.commands.parsing.ArgumentSettingsBuilder;
import bot.commands.parsing.MessageArguments;


public abstract class AbstractBotCommand {

    protected CommandInfo commandInfo;
    protected PlayerAccessLevel accessLevel;
    protected ArgumentSettings argumentSettings;

    public AbstractBotCommand(CommandInfo commandInfo) {
        this(commandInfo, PlayerAccessLevel.USER);
    }

    public AbstractBotCommand(CommandInfo commandInfo, PlayerAccessLevel playerAccessLevel) {
        this(commandInfo, playerAccessLevel, ArgumentSettingsBuilder.getBuilder(commandInfo).build());
    }

    public AbstractBotCommand(CommandInfo commandInfo, ArgumentSettings argumentSettings) {
        this(commandInfo, PlayerAccessLevel.USER, argumentSettings);
    }

    public AbstractBotCommand(CommandInfo commandInfo,
                              PlayerAccessLevel playerAccessLevel,
                              ArgumentSettings argumentSettings) {
        this.commandInfo = commandInfo;
        this.accessLevel = playerAccessLevel;
        this.argumentSettings = argumentSettings;
    }

    protected void setArguments(ArgumentSettings argumentSettings) {
        this.argumentSettings = argumentSettings;
    }

    public abstract void handle(CommandParameters parameters);

    public MessageArguments parseArguments(String commandString) {
        return argumentSettings.parseArguments(commandString);
    }

    public String[] getCommandNames() {
        return commandInfo.getCommandNames();
    }

    public PlayerAccessLevel getCommandAccessLevel() {
        return accessLevel;
    }

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }
}
