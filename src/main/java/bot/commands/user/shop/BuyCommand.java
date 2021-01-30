package bot.commands.user.shop;

import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;

public class BuyCommand extends AbstractBuyCommand {

    public BuyCommand(AnimeCardsGame game) {
        super(game);
        name = "buy";
        guildOnly = false;
        help = "buy a specific item from power ups shop";
    }

    @Override
    public void handle(CommandEvent event) {
        buyItem(event, game.getPowerUpsShop(), player, commandArgs.getString());
    }
}
