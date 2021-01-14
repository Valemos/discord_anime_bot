package bot.commands.user.inventory;

import bot.commands.AbstractCommandOptionalPlayer;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.items.MaterialsSet;

public class MaterialsCommand extends AbstractCommandOptionalPlayer {

    public MaterialsCommand(AnimeCardsGame game) {
        super(game);
        name = "materials";
        aliases = new String[]{"mats"};
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        if(tryFindPlayerArgument(event)){
            return;
        }

        MaterialsSet materials = requestedPlayer.getMaterials();
        sendMessage(event, "Player materials:\n" + materials.getDescriptionMultiline());
    }
}
