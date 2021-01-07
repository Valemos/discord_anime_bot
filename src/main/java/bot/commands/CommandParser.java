package bot.commands;

import java.util.ArrayList;
import java.util.List;

public class CommandParser {

    public static class CommandParseException extends Exception {
        public CommandParseException(String message) {
            super(message);
        }
    }

    public static String getCommandName(String commandString) {
        if (!stringIsCommand(commandString)){
            return "";
        }

        try{
            int nameStart = commandString.indexOf(CommandInfo.commandChar) + 1;
            int nameEnd = commandString.indexOf(' ');
            if (nameEnd == -1) nameEnd = commandString.length();

            return commandString.substring(nameStart, nameEnd);
        }catch(IndexOutOfBoundsException e){
            return "";
        }
    }

    public static boolean stringIsCommand(String string) {
        return string.startsWith(CommandInfo.commandChar);
    }

    public static List<String> getStringCommandParts(String commandString) throws CommandParseException {
        if (!stringIsCommand(commandString)){
            throw new CommandParseException(String.format("\"%s\"is not a valid command", commandString));
        }


        List<String> parts = new ArrayList<>();

        int startIndex, endIndex = 0;

        while (true){
            startIndex = findArgumentStart(endIndex, commandString);
            endIndex = findArgumentDelimiterEnd(startIndex, commandString);

            if (startIndex >= commandString.length()){
                break;
            }else{
                parts.add(getArgumentWithoutQuotes(commandString, startIndex, endIndex));
                endIndex++;
            }
        }

        return parts;
    }

    private static String getArgumentWithoutQuotes(String commandString, int startIndex, int endIndex) {
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

    private static int findArgumentDelimiterEnd(int index, String commandString) throws CommandParseException {
        boolean firstQuoteFound = false;
        while(index < commandString.length()){

            char currentChar = commandString.charAt(index);
            if (currentChar == '"'){
                if (firstQuoteFound){
                    firstQuoteFound = false;
                    break;
                }else{
                    firstQuoteFound = true;
                }
            } else if (currentChar == ' ' && !firstQuoteFound){
                index--;
                break;
            }

            index++;
        }

        if (firstQuoteFound){
            throw new CommandParseException(String.format("second quote not found in \"%s\"", commandString));
        }

        return index;
    }
}
