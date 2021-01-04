package bot.commands;

import bot.AccessLevel;
import game.AnimeCardsGame;

public abstract class BotCommandHandler {

    protected AccessLevel accessLevel = AccessLevel.USER;
    protected String commandName = "";

    public abstract void handleCommand(AnimeCardsGame game);

    public String getName() {
        return commandName;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }
}
