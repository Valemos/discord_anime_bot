package bot.commands.user.inventory;

import bot.commands.AbstractCommandOptionalPlayer;
import bot.commands.MenuCreator;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.Player;
import game.items.ItemsPersonalManager;

public class InventoryCommand extends AbstractCommandOptionalPlayer {

    public InventoryCommand(AnimeCardsGame game) {
        super(game);
        name = "inventory";
        aliases = new String[]{"inv"};
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        if(tryFindPlayerArgument(event)){
            return;
        }

        ItemsPersonalManager itemsManager = game.getItemsPersonal();

        MenuCreator.showMenuForItemStats(
                itemsManager.getElement(requestedPlayer.getId()).sortedByPower(),
                event,
                game
        );
    }

}
