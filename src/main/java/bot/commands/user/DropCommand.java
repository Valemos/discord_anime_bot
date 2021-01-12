package bot.commands.user;


import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import game.cards.CollectionGlobal;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;

public class DropCommand extends AbstractCommand<AbstractCommand.NoArgumentsConfig> {

    public DropCommand(AnimeCardsGame game) {
        super(game, NoArgumentsConfig.class);
        name = "drop";
    }

    @Override
    protected void handle(CommandEvent event) {
        CollectionGlobal collection = game.getGlobalCollection();
        List<CardGlobal> cards = collection.getRandomCards(3);
        displayCards(cards, event.getChannel());
    }

    private void displayCards(List<CardGlobal> cards, MessageChannel channel) {
        StringBuilder builder = new StringBuilder();
        if (!cards.isEmpty()) {
            createCardsMessage(cards, builder);
            channel.sendMessage(builder.toString()).queue();
        } else {
            channel.sendMessage("There were no cards dropped").queue();
        }
    }

    private void createCardsMessage(List<CardGlobal> cards, StringBuilder messageBuilder) {
        messageBuilder.append("Dropped cards:");

        for (CardGlobal card : cards) {
            messageBuilder.append('\n').append(card.getOneLineString());
        }
    }
}