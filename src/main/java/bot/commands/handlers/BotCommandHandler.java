package bot.commands.handlers;

import bot.AccessLevel;
import bot.commands.CommandInfo;
import bot.commands.CommandParameters;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class BotCommandHandler extends CommandWithArguments {

    protected CommandInfo commandInfo;
    protected AccessLevel accessLevel;
    protected MessageArguments arguments;

    public BotCommandHandler(CommandInfo commandInfo) {
        this(commandInfo, AccessLevel.USER, EmptyMessageArguments.class);
    }

    public BotCommandHandler(CommandInfo commandInfo, AccessLevel accessLevel) {
        this(commandInfo, accessLevel, EmptyMessageArguments.class);
    }

    public BotCommandHandler(CommandInfo commandInfo, Class<? extends MessageArguments> argumentsClass) {
        this(commandInfo, AccessLevel.USER, argumentsClass);
    }

    public BotCommandHandler(CommandInfo commandInfo,
                             AccessLevel accessLevel,
                             Class<? extends MessageArguments> argumentsClass) {
        this.commandInfo = commandInfo;
        this.accessLevel = accessLevel;
        this.arguments = createArgumentsInstance(argumentsClass);
    }

    private MessageArguments createArgumentsInstance(Class<? extends MessageArguments> argumentsClass) {
        try {
            return argumentsClass.getConstructor(CommandInfo.class).newInstance(commandInfo);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Logger.getGlobal().log(Level.SEVERE, "cannot create arguments class instance");
            throw new RuntimeException(e);
        }
    }

    public abstract void handleCommand(CommandParameters parameters);

    @Override
    public MessageArguments getArguments(String commandString) {
        try {
            return arguments.fromString(commandString);

        } catch (MessageArguments.InvalidCommandException e) {
            Logger.getGlobal().log(Level.SEVERE, "invalid command parsed. programmer error");
            throw new RuntimeException(e);
        }
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
