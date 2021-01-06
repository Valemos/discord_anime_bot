package bot.commands.handlers.creator;

import bot.AccessLevel;
import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.CreateCardArguments;
import bot.commands.handlers.BotCommandHandler;
import game.cards.CardStats;
import game.cards.CharacterCard;

public class CreateCardHandler extends BotCommandHandler {

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
