package bot.commands.handlers;

import com.jagrosh.jdautilities.command.Command;
import game.AnimeCardsGame;

public abstract class AbstractCommand extends Command {
    protected AnimeCardsGame game;

    public AbstractCommand(AnimeCardsGame game) {
        this.game = game;
    }
}
