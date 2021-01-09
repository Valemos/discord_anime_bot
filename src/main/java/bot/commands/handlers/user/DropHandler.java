package bot.commands.handlers.user;


import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.handlers.BotCommandHandler;
import game.cards.CharacterCardGlobal;
import game.cards.GlobalCollection;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;

public class DropHandler extends BotCommandHandler {

    public DropHandler() {
        super(new CommandInfo("drop"));
    }

    @Override
    public void handle(CommandParameters parameters) {

        GlobalCollection collection = parameters.game.getGlobalCollection();
        List<CharacterCardGlobal> cards = collection.getRandomCards(3);
        displayCards(parameters.channel, cards);
    }

    private void displayCards(MessageChannel channel, List<CharacterCardGlobal> cards) {
        StringBuilder builder = new StringBuilder();
        if (!cards.isEmpty()) {
            createCardsMessage(cards, builder);
            channel.sendMessage(builder.toString()).queue();
        } else {
            channel.sendMessage("There were no cards dropped").queue();
        }
    }

    private void createCardsMessage(List<CharacterCardGlobal> cards, StringBuilder messageBuilder) {
        messageBuilder.append("Dropped cards:");

        for (CharacterCardGlobal card : cards) {
            messageBuilder.append('\n');
            messageBuilder.append(card.getOneLineString());
        }
    }
}