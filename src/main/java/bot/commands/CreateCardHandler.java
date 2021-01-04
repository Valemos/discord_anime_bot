package bot.commands;

import bot.AccessLevel;
import game.AnimeCardsGame;

public class CreateCardHandler extends BotCommandHandler{

    public CreateCardHandler() {
        commandName = "createcard";
        accessLevel = AccessLevel.CREATOR;
    }

    @Override
    public void handleCommand(AnimeCardsGame game) {

    }
}
