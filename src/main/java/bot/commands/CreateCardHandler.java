package bot.commands;

import bot.AccessLevel;
import game.CardStats;
import game.CharacterCard;

public class CreateCardHandler extends BotCommandHandler{

    public CreateCardHandler() {
        commandInfo = new CommandInfo("createcard");
        accessLevel = AccessLevel.CREATOR;
    }

    @Override
    public void handleCommand(CommandParameters parameters) {
        CreateCardArguments arguments = (CreateCardArguments) parameters.messageArgs;

        CharacterCard new_card = new CharacterCard(
                arguments.characterName,
                arguments.seriesName,
                arguments.imageUrl,
                new CardStats());

        parameters.game.addCard(new_card);
    }
}
