package bot.commands.arguments;

public class StringArgument extends ArgumentParser {

    public StringArgument(String name) {
        super(name);
    }

    @Override
    protected ArgumentContent parse(String commandString, int startIndex) throws ArgumentParseException {
        return getNextArgumentContent(commandString, startIndex);
    }

    public String getName() {
        return name;
    }
}
