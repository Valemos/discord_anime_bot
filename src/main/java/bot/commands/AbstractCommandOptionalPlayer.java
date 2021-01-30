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

    @Override
    public void handle(CommandEvent event) {
        if (tryFindPlayerArgument(event)) {
            handlePlayer(event);
        }
    }

    protected abstract void handlePlayer(CommandEvent event);

    public boolean tryFindPlayerArgument(CommandEvent event) {
        if (commandArgs.id != null) {
            requestedPlayer = game.getPlayer(commandArgs.id);

            if (requestedPlayer == null){
                event.getChannel().sendMessage(commandArgs.id + " player not found").queue();
                return false;
            }
        } else {
            requestedPlayer = player;
        }
        return true;
    }
}
