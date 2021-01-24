package bot.commands.admin;

import bot.commands.AbstractCommandOptionalPlayer;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;

public class ResetCooldownsCommand extends AbstractCommandOptionalPlayer {

    public ResetCooldownsCommand(AnimeCardsGame game) {
        super(game);
        name = "resetcd";
        requiredRole = "admin";
        help = "resets all cooldowns for player with provided id";
    }

    @Override
    public void handle(CommandEvent event) {
        if(!tryFindPlayerArgument(event)) return;

        player.getCooldowns().reset();

        sendMessage(event, "cooldowns reset for " + player.getId());
    }
}
