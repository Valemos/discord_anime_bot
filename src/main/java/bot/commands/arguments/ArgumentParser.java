package bot.commands.arguments;

import java.util.ArrayList;
import java.util.List;

public abstract class ArgumentParser {

    String name;

    public ArgumentParser(String name) {
        this.name = name;
    }

    protected abstract ArgumentContent parse(String commandString, int startIndex) throws ArgumentParseException, InvalidCommandException;

    public static List<ArgumentContent> getStringArguments(String commandString) throws ArgumentParseException {
        List<ArgumentContent> parts = new ArrayList<>();

        ArgumentContent argument = getNextArgumentContent(commandString, 0);
        while (argument.exists()){
            parts.add(argument);
            argument = getNextArgumentContent(commandString, argument.end + 1);
        }
        return parts;
    }

    public static ArgumentContent getNextArgumentContent(String commandString, int searchStart) throws ArgumentParseException {

        int startIndex = findArgumentStart(searchStart, commandString);
        int endIndex = findArgumentEnd(startIndex, commandString);

        if (startIndex < commandString.length()){
            String commandContent = getStringWithoutQuotes(commandString, startIndex, endIndex);
            return new ArgumentContent(commandContent, startIndex, endIndex);
        }else{
            return new ArgumentContent(null, startIndex, endIndex);
        }
    }

    public static String getStringWithoutQuotes(String commandString, int startIndex, int endIndex) {
        commandString = commandString.trim();
        if (commandString.charAt(startIndex) == '"' &&
                commandString.charAt(endIndex) == '"'){
            return commandString.substring(startIndex + 1, endIndex);
        }else{
            if (endIndex != commandString.length()){
                endIndex++;
            }
            return commandString.substring(startIndex, endIndex);
        }
    }

    private static int findArgumentStart(int index, String commandString) {
        while(index < commandString.length()){
            char currentChar = commandString.charAt(index);

            if (currentChar == ' ') {
                index++;
            }else{
                break;
            }
        }

        return index;
    }

    private static int findArgumentEnd(int index, String commandString) throws ArgumentParseException {
        while(index < commandString.length()){

            char currentChar = commandString.charAt(index);
            if (currentChar == '"'){
                index = findNextQuote(commandString, index + 1);
            } else if (currentChar == ' '){
                index--;
                break;
            }else{
                index++;
            }
        }
        return index;
    }

    private static int findNextQuote(String commandString, int index) throws ArgumentParseException{
        while (index < commandString.length()) {
            if(commandString.charAt(index++) == '"'){
                break;
            }
        }

        if (index == commandString.length()){
            throw new ArgumentParseException("second quote not found in \"" + commandString + '"');
        }

        return index;
    }

    public String getArgumentNotFoundMessage() {
        return String.format("argument \"%s\" not provided", name);
    }

    public static class InvalidCommandException extends Exception {
        public InvalidCommandException(String message) {
            super(message);
        }
    }

    static class ArgumentParseException extends Exception {
        public ArgumentParseException(String message) {
            super(message);
        }
    }
}
