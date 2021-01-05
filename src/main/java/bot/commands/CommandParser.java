package bot.commands;

public abstract class CommandParser {
    protected static final String commandPrefix = "#";

    public abstract String[] getArguments(String command);


    public static String getCommandName(String commandString) {
        if (!stringIsCommand(commandString)){
            return "";
        }

        try{
            int nameStart = commandString.indexOf(commandPrefix) + 1;
            int nameEnd = commandString.indexOf(' ');
            if (nameEnd == -1) nameEnd = commandString.length();

            return commandString.substring(nameStart, nameEnd);
        }catch(IndexOutOfBoundsException e){
            return "";
        }
    }

    public static boolean stringIsCommand(String string) {
        return string.startsWith(commandPrefix);
    }

    public static String[] getStringCommandParts(String string){
        if (!stringIsCommand(string)){
            return null;
        }

        if (string.contains(" ")){
            return string.split(" ");
        }else{
            return new String[]{string};
        }
    }
}
