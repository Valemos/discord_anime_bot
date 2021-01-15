package bot.commands.user.stocks;

import bot.commands.AbstractCommand;
import bot.commands.arguments.MultipleWordsArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.stocks.StocksPersonal;

public class StockValueCommand extends AbstractCommand<MultipleWordsArguments> {
    public StockValueCommand(AnimeCardsGame game) {
        super(game, MultipleWordsArguments.class);
        name = "seriesvalue";
        guildOnly = false;
    }

    @Override
    public void handle(CommandEvent event) {
        String seriesName = commandArgs.getSingleString();

        StocksPersonal playerStocks = game.getStocks(player.getId());
        String stockName = playerStocks.findByNamePart(seriesName);
        if (stockName != null){
            sendMessage(event, playerStocks.getStockStringValue(stockName));
        }else{
            sendMessage(event, "stock not found");
        }
    }
}
