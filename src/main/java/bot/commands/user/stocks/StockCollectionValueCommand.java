package bot.commands.user.stocks;

import bot.commands.AbstractCommandOptionalPlayer;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.stocks.StocksPersonal;

public class StockCollectionValueCommand extends AbstractCommandOptionalPlayer {

    public StockCollectionValueCommand(AnimeCardsGame game) {
        super(game);
        name = "collectionvalue";
        guildOnly = false;
    }

    @Override
    public void handle(CommandEvent event) {
        if(tryFindPlayerArgument(event)){
            return;
        }

        StocksPersonal stocks = game.getStocks(requestedPlayer.getId());
        float totalValue = 0;
        for (String stockName : stocks.getNames()){
            totalValue += stocks.getStockValue(stockName);
        }

        sendMessage(event, "total stocks for " + requestedPlayer.getId() + ": " + totalValue);
    }
}
