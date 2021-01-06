package bot.commands;

public class CreateCardArguments extends DefaultMessageArguments{

    public String characterName;
    public String seriesName;
    public String imageUrl;

    public CreateCardArguments(CommandInfo commandInfo) {
        super(commandInfo);
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public CreateCardArguments fromString(String commandMessage) {
        return null;
    }
}
