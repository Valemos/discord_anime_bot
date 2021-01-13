package bot.commands.user;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.Paginator;
import game.AnimeCardsGame;
import game.stocks.StocksPersonal;
import org.kohsuke.args4j.Argument;

public class ShowStocksCommand extends AbstractCommand<ShowStocksCommand.Arguments> {

    public static class Arguments{
        @Argument(usage = "optional id to show stocks of specific player")
        String playerId;
    }

    public ShowStocksCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "stocks";
    }

    @Override
    protected void handle(CommandEvent event) {
        String playerId = commandArgs.playerId == null? player.getId() : commandArgs.playerId;

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
