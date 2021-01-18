package bot.commands.creator;

import bot.commands.AbstractCommandOptionalPlayer;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;

public class ResetCooldownsCommand extends AbstractCommandOptionalPlayer {

    public ResetCooldownsCommand(AnimeCardsGame game) {
        super(game);
        name = "resetcd";
        requiredRole = "admin";
    }

    @Override
    public void handle(CommandEvent event) {
        String playerId = commandArgs.getSelectedOrPlayerId(player);

        game.getCooldowns(playerId).reset();

        sendMessage(event, "cooldowns reset for " + playerId);
    }
}
