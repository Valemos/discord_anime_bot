package bot.commands.user.inventory;

import bot.commands.AbstractCommandOptionalPlayer;
import bot.menu.BotMenuCreator;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.items.ItemsPersonalManager;

public class InventoryCommand extends AbstractCommandOptionalPlayer {

    public InventoryCommand(AnimeCardsGame game) {
        super(game);
        name = "inventory";
        aliases = new String[]{"inv"};
        guildOnly = false;
    }

    @Override
    public void handle(CommandEvent event) {
        if(tryFindPlayerArgument(event)){
            return;
        }

        ItemsPersonalManager itemsManager = game.getItemsPersonal();

        BotMenuCreator.menuForItemStats(
                itemsManager.getElement(requestedPlayer.getId()).sortedByItemPower(),
                event,
                game,
                1
        );
    }

}
