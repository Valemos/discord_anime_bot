package bot.commands.handlers.creator;

import bot.AccessLevel;
import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.handlers.BotCommandHandler;
import game.cards.CardStatsGlobal;
import game.cards.CharacterCardGlobal;

public class CreateGlobalCardHandler extends BotCommandHandler {

    public CreateGlobalCardHandler() {
        super(new CommandInfo("createcard"), AccessLevel.CREATOR, CreateCardArguments.class);
    }

    @Override
    public void handleCommand(CommandParameters parameters) {
        CreateCardArguments arguments = (CreateCardArguments) parameters.messageArgs;

        CharacterCardGlobal new_card = new CharacterCardGlobal(
                arguments.characterName,
                arguments.seriesName,
                arguments.imageUrl,
                new CardStatsGlobal());

        parameters.game.addCard(new_card);
    }
}
