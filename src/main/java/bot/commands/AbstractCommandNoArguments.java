package bot.commands;

import bot.CommandPermissions;
import game.AnimeCardsGame;

public abstract class AbstractCommandNoArguments extends AbstractCommand<AbstractCommandNoArguments.EmptyArguments> {
    public static class EmptyArguments {}

    public AbstractCommandNoArguments(AnimeCardsGame game, CommandPermissions accessLevel) {
        super(game, EmptyArguments.class, accessLevel);
    }
}
