package bot.commands.user.stocks;

import bot.commands.AbstractCommandOptionalPlayer;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.Paginator;
import game.AnimeCardsGame;
import game.player_objects.StockValue;

public class ShowStocksCommand extends AbstractCommandOptionalPlayer {

    public ShowStocksCommand(AnimeCardsGame game) {
        super(game);
        name = "stocks";
        guildOnly = false;
        help = "shows all stock values of player with provided id";
    }

    @Override
    public void handlePlayer(CommandEvent event) {

        String[] stockItems = requestedPlayer.getStocks().stream()
                .sorted((s1, s2) -> Float.compare(s1.getValue(), s2.getValue()))
                .map(StockValue::getNameValue)
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
