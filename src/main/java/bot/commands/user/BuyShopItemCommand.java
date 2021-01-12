package bot.commands.user;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.shop.ItemShop;
import org.kohsuke.args4j.Argument;

public class BuyShopItemCommand extends AbstractCommand<BuyShopItemCommand.Arguments> {
    public static class Arguments {
        @Argument(required=true)
        String itemId;
    }

    public BuyShopItemCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "buy";
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        ItemShop shop = game.getItemShop();
        if (!shop.tryBuyItem(player, commandArgs.itemId)){
            sendMessage(event, "cannot buy this item");
        }
    }
}
