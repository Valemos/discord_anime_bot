package bot.commands.user.stocks;

import bot.commands.AbstractCommand;
import bot.commands.arguments.MultipleWordsArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.player_objects.StockValue;

public class StockValueCommand extends AbstractCommand<MultipleWordsArguments> {
    public StockValueCommand(AnimeCardsGame game) {
        super(game, MultipleWordsArguments.class);
        name = "seriesvalue";
        guildOnly = false;
        help = "shows stock value for specific series of player with provided id";
    }

    @Override
    public void handle(CommandEvent event) {
        String seriesName = commandArgs.getSingleString();

        StockValue stock = player.findStockByName(seriesName);
        if (stock != null){
            sendMessage(event, stock.getNameValue());
        }else{
            sendMessage(event, "stock not found");
        }
    }
}
