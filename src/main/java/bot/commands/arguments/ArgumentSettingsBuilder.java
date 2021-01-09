package bot.commands.arguments;

import bot.commands.CommandInfo;

import java.util.ArrayList;
import java.util.List;

public class ArgumentSettingsBuilder {

    private final CommandInfo commandInfo;
    private final List<ArgumentParser> requiredArguments;
    private final List<ArgumentParser> optionalArguments;


    private ArgumentSettingsBuilder(CommandInfo commandInfo) {
        this.commandInfo = commandInfo;
        requiredArguments = new ArrayList<>();
        optionalArguments = new ArrayList<>();
    }

    public static ArgumentSettingsBuilder getBuilder(CommandInfo commandInfo) {
        return new ArgumentSettingsBuilder(commandInfo);
    }

    public ArgumentSettingsBuilder addRequired(ArgumentParser argument) {
        requiredArguments.add(argument);
        return this;
    }

    public ArgumentSettingsBuilder addOptional(ArgumentParser argument) {
        optionalArguments.add(argument);
        return this;
    }

    public ArgumentSettings build() {
        return new ArgumentSettings(commandInfo, requiredArguments, optionalArguments);
    }
}
