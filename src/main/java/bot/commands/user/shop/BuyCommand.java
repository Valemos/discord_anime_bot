package bot.commands.user.shop;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.shop.ItemsShop;
import org.kohsuke.args4j.Argument;

public class BuyCommand extends AbstractCommand<BuyCommand.Arguments> {
    public static class Arguments {
        @Argument(metaVar = "item id", required=true)
        String itemId;
    }

    public BuyCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "buy";
        guildOnly = false;
    }

    @Override
    public void handle(CommandEvent event) {
        ItemsShop shop = game.getItemsShop();
        if (shop.tryBuyItem(player, commandArgs.itemId)){
            sendMessage(event, String.format("you bought \"%s\"", shop.getLastItemSelected().getName()));
        }else{
            sendMessage(event,
                    "cannot buy this item\nyou have:\n"
                    + player.getMaterials().getDescriptionMultiline()
                    + "\nItem costs:\n"
                    + shop.getLastItemSelected().getMaterialsCost().getDescriptionMultiline()
            );
        }
    }
}
