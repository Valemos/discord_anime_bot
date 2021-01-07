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
        return characterName != null && seriesName != null && imageUrl != null;
    }

    @Override
    public CreateCardArguments fromString(String commandMessage) {
        super.fromString(commandMessage);

        if (commandParts.size() == 3){
            characterName = commandParts.get(0);
            seriesName = commandParts.get(1);
            imageUrl = commandParts.get(2);
            return this;
        }else{
            return null;
        }
    }
}
