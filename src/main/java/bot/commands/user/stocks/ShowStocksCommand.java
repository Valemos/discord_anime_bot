package bot.commands.user.stocks;

import bot.commands.AbstractCommand;
import bot.commands.OptionalIdentifierArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.Paginator;
import game.AnimeCardsGame;
import game.stocks.StocksPersonal;

public class ShowStocksCommand extends AbstractCommand<OptionalIdentifierArguments> {

    public ShowStocksCommand(AnimeCardsGame game) {
        super(game, OptionalIdentifierArguments.class);
        name = "stocks";
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        String playerId = commandArgs.getSelectedOrPlayerId(player);

        StocksPersonal stocks = game.getStocks(playerId);

        String[] stockItems = stocks.getNames().stream()
                .sorted((name1, name2) ->
                        Float.compare(
                                stocks.getStockValue(name1),
                                stocks.getStockValue(name2)))
                .map(stocks::getStockStringValue)
                .toArray(String[]::new);

        Paginator stocksMenu = new Paginator.Builder()
                .setEventWaiter(game.getEventWaiter())
                .setText("Stock values for anime series")
                .setUsers(event.getAuthor())
                .setItems(stockItems)
                .setItemsPerPage(10)
                .build();

        stocksMenu.display(event.getChannel());
    }
}
