package bot.commands;

import bot.commands.arguments.OptionalIdentifierArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.Player;

public abstract class AbstractCommandOptionalPlayer extends AbstractCommand<OptionalIdentifierArguments>{
    protected Player requestedPlayer;

    public AbstractCommandOptionalPlayer(AnimeCardsGame game) {
        super(game, OptionalIdentifierArguments.class);
    }


    public boolean tryFindPlayerArgument(CommandEvent event) {
        String playerId = commandArgs.getSelectedOrPlayerId(player);
        requestedPlayer = game.getPlayerById(playerId);

        if (requestedPlayer == null){
            event.getChannel().sendMessage(playerId + " player not found").queue();
            return false;
        }

        return true;
    }
}
