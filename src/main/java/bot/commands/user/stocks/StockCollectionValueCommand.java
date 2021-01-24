package bot.commands.user.stocks;

import bot.commands.AbstractCommandOptionalPlayer;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.player_objects.StockValue;

public class StockCollectionValueCommand extends AbstractCommandOptionalPlayer {

    public StockCollectionValueCommand(AnimeCardsGame game) {
        super(game);
        name = "collectionvalue";
        guildOnly = false;
        help = "shows total sum of stock values of player with provided id";
    }

    @Override
    public void handle(CommandEvent event) {
        if(tryFindPlayerArgument(event)){
            return;
        }

        float totalValue = 0;
        for (StockValue stock : requestedPlayer.getStocks()){
            totalValue += stock.getValue();
        }

        sendMessage(event, "total stocks for " + requestedPlayer.getId() + ": " + totalValue);
    }
}
