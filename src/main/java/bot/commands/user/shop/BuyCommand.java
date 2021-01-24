package bot.commands.user.shop;

import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.shop.ItemsShop;

public class BuyCommand extends AbstractBuyCommand {

    public BuyCommand(AnimeCardsGame game) {
        super(game);
        name = "buy";
        guildOnly = false;
        help = "buys a specific item from power ups shop";
    }

    @Override
    public void handle(CommandEvent event) {
        ItemsShop shop = game.getItemsShop();
        buyItem(event, shop, player, commandArgs.itemId);
    }
}
