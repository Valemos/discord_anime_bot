package bot.commands;

import bot.CommandPermissions;
import game.AnimeCardsGame;

public abstract class AbstractCommandNoArguments extends AbstractCommand<AbstractCommandNoArguments.EmptyArguments> {
    public static class EmptyArguments {}

    public AbstractCommandNoArguments(AnimeCardsGame game) {
        this(game, CommandPermissions.USER);
    }

    public AbstractCommandNoArguments(AnimeCardsGame game, CommandPermissions commandPermissions) {
        super(game, EmptyArguments.class, commandPermissions);
    }
}
