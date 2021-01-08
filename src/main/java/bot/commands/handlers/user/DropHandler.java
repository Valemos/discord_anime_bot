package bot.commands.handlers.user;


import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.handlers.BotCommandHandler;
import game.cards.CharacterCardGlobal;
import game.cards.GlobalCollection;

import java.util.List;

public class DropHandler extends BotCommandHandler {

    public DropHandler() {
        super(new CommandInfo("drop"));
    }

    @Override
    public void handleCommand(CommandParameters parameters) {

        GlobalCollection collection = parameters.game.getGlobalCollection();
        List<CharacterCardGlobal> cards = collection.getRandomCards(3);

        StringBuilder messageBuilder = new StringBuilder();
        if (!cards.isEmpty()) {
            createCardsMessage(cards, messageBuilder);
        } else {
            messageBuilder.append("There were no cards dropped");
        }

        parameters.channel.sendMessage(messageBuilder.toString()).queue();
    }

    private void createCardsMessage(List<CharacterCardGlobal> cards, StringBuilder messageBuilder) {
        messageBuilder.append("Dropped new 3 cards:");

        for (CharacterCardGlobal card : cards) {
            messageBuilder.append('\n');
            messageBuilder.append(card.getOneLineString());
        }
    }
}