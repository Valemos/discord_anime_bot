package bot.commands.user.inventory;

import bot.commands.AbstractCommandOptionalPlayer;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.materials.MaterialsSet;

public class MaterialsCommand extends AbstractCommandOptionalPlayer {

    public MaterialsCommand(AnimeCardsGame game) {
        super(game);
        name = "materials";
        aliases = new String[]{"mats"};
        guildOnly = false;
        help = "shows all materials for player with provided id";
    }

    @Override
    public void handlePlayer(CommandEvent event) {
        MaterialsSet materials = requestedPlayer.getMaterials();
        sendMessage(event, "Player materials:\n" + materials.getDescriptionMultiline());
    }
}
