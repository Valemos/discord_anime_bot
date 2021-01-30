package bot.commands.user;

import bot.commands.AbstractCommandOptionalPlayer;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.player_objects.CooldownSet;

import java.time.Instant;

public class CooldownCommand extends AbstractCommandOptionalPlayer {

    public CooldownCommand(AnimeCardsGame game) {
        super(game);
        name = "cooldown";
        aliases = new String[]{"cd"};
    }

    @Override
    protected void handlePlayer(CommandEvent event) {
        CooldownSet cooldown = requestedPlayer.getCooldowns();
        sendMessage(event, cooldown.getDescription(Instant.now()));
    }
}
