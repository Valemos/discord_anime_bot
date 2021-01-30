package bot.commands.user.shop;

import bot.commands.AbstractCommand;
import bot.commands.arguments.MultipleWordsArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.shop.PowerUpsShop;
import game.shop.items.UsablePowerUp;

public class UsePowerUpCommand extends AbstractCommand<MultipleWordsArguments> {
    public UsePowerUpCommand(AnimeCardsGame game) {
        super(game, MultipleWordsArguments.class);
        name = "use";
        help = "uses existing power up from your inventory";
    }

    @Override
    public void handle(CommandEvent event) {
        String itemId = commandArgs.getString();

        PowerUpsShop shop = game.getPowerUpsShop();
        UsablePowerUp itemFound = shop.findShopItem(itemId);
        if (itemFound == null){
            sendMessage(event, "item \"" + itemId + "\" doesn't exist in item shop");
            return;
        }

        if(!player.canUseItem(itemFound)){
            sendMessage(event, "you cannot use "+ itemFound.getName() +" right now");
            return;
        }

        player.usePowerUp(game, itemFound);
        sendMessage(event, itemFound.getName() + " was used");
    }
}
