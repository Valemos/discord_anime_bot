package bot.commands.user.shop;

import bot.commands.AbstractCommand;
import bot.commands.arguments.BuyItemArguments;
import bot.commands.arguments.MultipleWordsArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.Player;
import game.shop.AbstractShop;
import game.shop.items.AbstractShopItem;
import game.shop.items.IShopItem;

public abstract class AbstractBuyCommand extends AbstractCommand<MultipleWordsArguments> {

    public AbstractBuyCommand(AnimeCardsGame game) {
        super(game, MultipleWordsArguments.class);
    }

    protected <T extends IShopItem> void buyItem(CommandEvent event, AbstractShop<T> shop, Player player, String itemId) {

        T item = shop.findShopItem(itemId);
        if (item == null){
            sendMessage(event, "item not found \"" + itemId + '"');
            return;
        }

        if (shop.tryBuyItem(game, player, item)){
            sendMessage(event, String.format("you bought \"%s\"", item.getName()));
        }else{
            sendMessage(event, shop.getMessageCostIsHigh(item, player)
            );
        }
    }
}
