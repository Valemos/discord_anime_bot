package bot.commands.user.inventory;

import bot.commands.AbstractCommandOptionalPlayer;
import bot.menu.BotMenuCreator;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.materials.Material;

import java.util.ArrayList;
import java.util.List;

public class InventoryCommand extends AbstractCommandOptionalPlayer {

    public InventoryCommand(AnimeCardsGame game) {
        super(game);
        name = "inventory";
        aliases = new String[]{"inv"};
        guildOnly = false;
        help = "shows all armor items for player with provided id";
    }

    @Override
    public void handlePlayer(CommandEvent event) {
        List<String> items = new ArrayList<>();

        items.add(requestedPlayer.getMaterials().getAmountString(Material.GOLD));
        items.add(requestedPlayer.getMaterials().getAmountString(Material.DIAMOND));

        requestedPlayer.getArmorItems().forEach((armorItem) -> items.add(armorItem.getIdNameStats()));

        BotMenuCreator.showPagedMenu(event, game, "Inventory", 1, items.toArray(new String[0]));
    }

}
