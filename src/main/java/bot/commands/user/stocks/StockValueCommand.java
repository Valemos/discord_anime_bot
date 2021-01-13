package bot.commands.user.stocks;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.stocks.StocksPersonal;
import org.kohsuke.args4j.Argument;

import java.util.List;

public class StockValueCommand extends AbstractCommand<StockValueCommand.Arguments> {

    public static class Arguments{
        @Argument(usage = "series name to search for")
        List<String> seriesNameWords;
    }

    public StockValueCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "seriesvalue";
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        String seriesName = String.join(" ", commandArgs.seriesNameWords);

        StocksPersonal playerStocks = game.getStocks(player.getId());
        String stockName = playerStocks.findByNamePart(seriesName);
        if (stockName != null){
            sendMessage(event, playerStocks.getStockStringValue(stockName));
        }else{
            sendMessage(event, "stock not found");
        }
    }
}
