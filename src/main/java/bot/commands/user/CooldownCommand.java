package bot.commands.user;

import bot.commands.AbstractCommandOptionalPlayer;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;

import java.time.Instant;

public class CooldownCommand extends AbstractCommandOptionalPlayer {

    public CooldownCommand(AnimeCardsGame game) {
        super(game);
        name = "cooldown";
        aliases = new String[]{"cd"};
    }

    @Override
    public void handle(CommandEvent event) {
        String playerId = commandArgs.getSelectedOrPlayerId(player);

        CooldownSet cooldown = game.getCooldowns(playerId);

        sendMessage(event, cooldown.getDescription(Instant.now()));
    }
}
