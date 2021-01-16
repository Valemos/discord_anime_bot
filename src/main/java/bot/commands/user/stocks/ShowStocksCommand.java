package bot.commands.user.stocks;

import bot.commands.AbstractCommandOptionalPlayer;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.Paginator;
import game.AnimeCardsGame;
import game.stocks.StocksPersonal;

public class ShowStocksCommand extends AbstractCommandOptionalPlayer {

    public ShowStocksCommand(AnimeCardsGame game) {
        super(game);
        name = "stocks";
        guildOnly = false;
    }

    @Override
    public void handle(CommandEvent event) {
        if(tryFindPlayerArgument(event)){
            return;
        }

        StocksPersonal stocks = game.getStocks(requestedPlayer.getId());

        String[] stockItems = stocks.getNames().stream()
                .sorted((name1, name2) ->
                        Float.compare(
                                stocks.getStockValue(name2),
                                stocks.getStockValue(name1)))
                .map(stocks::getStockStringValue)
                .toArray(String[]::new);

        Paginator.Builder stocksMenu = new Paginator.Builder()
                .setEventWaiter(game.getEventWaiter())
                .setText("Stock values for anime series")
                .setUsers(event.getAuthor())
                .setItemsPerPage(10)
                .waitOnSinglePage(true);

        stocksMenu = stockItems.length > 0 ?
                stocksMenu.addItems(stockItems) :
                stocksMenu.addItems("No stocks");

        stocksMenu.build().display(event.getChannel());
    }
}
