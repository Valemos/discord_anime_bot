package bot.commands.user.inventory;

import bot.commands.AbstractCommandOptionalPlayer;
import bot.menu.BotMenuCreator;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;

public class InventoryCommand extends AbstractCommandOptionalPlayer {

    public InventoryCommand(AnimeCardsGame game) {
        super(game);
        name = "inventory";
        aliases = new String[]{"inv"};
        guildOnly = false;
        help = "shows all armor items for player with provided id";
    }

    @Override
    public void handle(CommandEvent event) {
        if(tryFindPlayerArgument(event)){
            return;
        }

        BotMenuCreator.menuForItemStats(requestedPlayer.getArmorItems(), event, game, 1);
    }

}
