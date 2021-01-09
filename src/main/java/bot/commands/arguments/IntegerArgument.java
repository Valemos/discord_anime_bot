package bot.commands.arguments;

public class IntegerArgument extends ArgumentParser {

    public IntegerArgument(String name) {
        super(name);
    }

    @Override
    protected ArgumentContent parse(String commandString, int startIndex) throws ArgumentParseException {
        return throwIfNotInteger(getNextArgumentContent(commandString, startIndex));
    }

    private ArgumentContent throwIfNotInteger(ArgumentContent argumentContent) throws ArgumentParseException {
        try {
            Integer.parseInt(argumentContent.content);
        } catch(NumberFormatException | NullPointerException e) {
            throw new ArgumentParseException("argument \"" + argumentContent.content + "\" is not an integer");
        }

        return argumentContent;
    }
}
