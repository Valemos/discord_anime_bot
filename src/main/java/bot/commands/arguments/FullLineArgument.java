package bot.commands.arguments;

public class FullLineArgument extends ArgumentParser {
    public FullLineArgument(String name) {
        super(name);
    }

    @Override
    protected ArgumentContent parse(String commandString, int startIndex) throws ArgumentParseException {

        if (commandString.length() <= startIndex){
            throw new ArgumentParseException("cannot get any values for " + name);
        }

        return new ArgumentContent(commandString.substring(startIndex), startIndex);
    }
}
