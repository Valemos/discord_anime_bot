package bot.commands.user.shop;

import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.shop.ArmorShop;

public class BuyArmorCommand extends AbstractBuyCommand {

    public BuyArmorCommand(AnimeCardsGame game) {
        super(game);
        name = "buyarmor";
        aliases = new String[]{"buya"};
        guildOnly = false;
        help = "buys a specific item from armor shop";
    }

    @Override
    public void handle(CommandEvent event) {
        ArmorShop shop = game.getArmorShop();
        buyItem(event, shop, player, commandArgs.itemId);
    }
}
