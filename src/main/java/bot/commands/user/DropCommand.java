package bot.commands.user;


import bot.CommandPermissions;
import bot.commands.AbstractCommandNoArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import game.cards.CardsGlobalManager;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;

public class DropCommand extends AbstractCommandNoArguments {

    public DropCommand(AnimeCardsGame game) {
        super(game, CommandPermissions.USER);
        name = "drop";
    }

    @Override
    protected void handle(CommandEvent event) {
        CardsGlobalManager collection = game.getCollection();
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
            messageBuilder.append('\n').append(card.getFullName());
        }
    }
}