package bot.commands;

import game.AnimeCardsGame;

public abstract class AbstractCommandNoArguments extends AbstractCommand<AbstractCommandNoArguments.EmptyArguments> {
    public static class EmptyArguments {}

    public AbstractCommandNoArguments(AnimeCardsGame game) {
        super(game, EmptyArguments.class);
    }
}
