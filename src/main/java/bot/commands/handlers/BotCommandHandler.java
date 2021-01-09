package bot.commands.handlers;

import bot.AccessLevel;
import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.arguments.ArgumentParser;
import bot.commands.arguments.ArgumentSettings;
import bot.commands.arguments.ArgumentSettingsBuilder;
import bot.commands.arguments.MessageArguments;


public abstract class BotCommandHandler {

    protected CommandInfo commandInfo;
    protected AccessLevel accessLevel;
    protected ArgumentSettings argumentSettings;

    public BotCommandHandler(CommandInfo commandInfo) {
        this(commandInfo, AccessLevel.USER);
    }

    public BotCommandHandler(CommandInfo commandInfo, AccessLevel accessLevel) {
        this(commandInfo, accessLevel, ArgumentSettingsBuilder.getBuilder(commandInfo).build());
    }

    public BotCommandHandler(CommandInfo commandInfo, ArgumentSettings argumentSettings) {
        this(commandInfo, AccessLevel.USER, argumentSettings);
    }

    public BotCommandHandler(CommandInfo commandInfo,
                             AccessLevel accessLevel,
                             ArgumentSettings argumentSettings) {
        this.commandInfo = commandInfo;
        this.accessLevel = accessLevel;
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

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }

    public ArgumentSettings getArgumentSettings() {
        return argumentSettings;
    }
}
