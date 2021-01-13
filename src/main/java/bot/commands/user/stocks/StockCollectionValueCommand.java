package bot.commands.user.stocks;

import bot.commands.AbstractCommand;
import bot.commands.OptionalIdentifierArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.stocks.StocksPersonal;

public class StockCollectionValueCommand extends AbstractCommand<OptionalIdentifierArguments> {

    public StockCollectionValueCommand(AnimeCardsGame game) {
        super(game, OptionalIdentifierArguments.class);
        name = "collectionvalue";
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        String playerId = commandArgs.getSelectedOrPlayerId(player);

        StocksPersonal stocks = game.getStocks(playerId);
        float totalValue = 0;
        for (String stockName : stocks.getNames()){
            totalValue += stocks.getStockValue(stockName);
        }

        sendMessage(event, "total stocks for " + playerId + ": " + totalValue);
    }
}
