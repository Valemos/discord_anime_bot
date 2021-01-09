package bot.commands.arguments;

import bot.commands.CommandInfo;

import java.util.ArrayList;
import java.util.List;

public class ArgumentSettings {
    private final CommandArgument commandParser;
    private final List<ArgumentParser> requiredArguments;
    private final List<ArgumentParser> optionalArguments;

    public ArgumentSettings(CommandInfo commandInfo,
                            List<ArgumentParser> requiredArguments,
                            List<ArgumentParser> optionalArguments) {

        this.commandParser = new CommandArgument(commandInfo);
        this.requiredArguments = requiredArguments;
        this.optionalArguments = optionalArguments;
    }

    public MessageArguments parseArguments(String commandString){
        MessageArguments arguments = new MessageArguments();

        ArgumentContent commandArgument;
        try {
            commandArgument = commandParser.parse(commandString, 0);
        } catch (ArgumentParser.InvalidCommandException | ArgumentParser.ArgumentParseException e) {
            arguments.setErrorMessage(e.getMessage());
            return arguments;
        }

        int lastIndex = parseRequiredArguments(commandString, commandArgument.end + 1, arguments);
        parseOptionalArguments(commandString, lastIndex, arguments);

        return arguments;
    }

    private int parseRequiredArguments(String commandString, int index, MessageArguments arguments) {
        if (index >= commandString.length()){
            return index;
        }

        for (ArgumentParser parser : requiredArguments){
            try {
                ArgumentContent content = parser.parse(commandString, index);
                arguments.add(content);

                index = content.end + 1;

            } catch (ArgumentParser.ArgumentParseException | ArgumentParser.InvalidCommandException e) {
                arguments.setErrorMessage(e.getMessage());
                return commandString.length();
            }
        }

        if (requiredArguments.size() > arguments.argumentContents.size()){
            int indexFirstArgument = arguments.argumentContents.size();
            arguments.setErrorMessage(getMessageRequiredArgumentsNotFound(indexFirstArgument));
            return commandString.length();
        }

        return index;
    }

    private String getMessageRequiredArgumentsNotFound(int startIndex) {
        StringBuilder builder = new StringBuilder();
        for (int i = startIndex; i < requiredArguments.size(); i++) {
            builder.append(requiredArguments.get(i).getArgumentNotFoundMessage());
            builder.append('\n');
        }
        return builder.toString();
    }

    private void parseOptionalArguments(String commandString, int currentIndex, MessageArguments arguments) {
        if (currentIndex >= commandString.length()){
            return;
        }


        List<ArgumentParser> optionalCopy = new ArrayList<>(optionalArguments);
        while (currentIndex < commandString.length() && !optionalCopy.isEmpty()){
            try {
                ArgumentContent content = tryParseWithSuitableParser(optionalCopy, commandString, currentIndex);
                currentIndex = content.end + 1;

            } catch (ArgumentParser.ArgumentParseException e) {
                arguments.setErrorMessage(e.getMessage());
                return;
            }
        }
    }

    private ArgumentContent tryParseWithSuitableParser(List<ArgumentParser> parsers, String commandString, int index) throws ArgumentParser.ArgumentParseException {

        for (int i = 0; i < parsers.size(); i++) {
            ArgumentParser parser = parsers.get(i);

            try{
                ArgumentContent content = parser.parse(commandString, index);
                parsers.remove(i);
                return content;

            }catch (ArgumentParser.InvalidCommandException | ArgumentParser.ArgumentParseException ignored){}
        }
        StringArgument defaultParser = new StringArgument("");
        ArgumentContent argument = defaultParser.parse(commandString, index);
        throw new ArgumentParser.ArgumentParseException("argument not valid \"" + argument.content + '"');
    }

    private ArgumentParser findAndRemoveMatchingOptional(List<ArgumentParser> optionalArguments, String commandString, int index) throws ArgumentParser.InvalidCommandException {

        throw new ArgumentParser.InvalidCommandException("unknown argument at position " + index);
    }
}
