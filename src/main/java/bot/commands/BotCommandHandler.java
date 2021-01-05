package bot.commands;

import bot.AccessLevel;
import game.AnimeCardsGame;

import java.util.Arrays;


public abstract class BotCommandHandler extends CommandParser {

    protected String commandName = "";

    protected AccessLevel accessLevel = AccessLevel.USER;

    public void handleCommand(AnimeCardsGame game) {
        handleCommand(game, null);
    }

    public abstract void handleCommand(AnimeCardsGame game, String[] arguments);

    @Override
    public String[] getArguments(String command) {
        String[] parts = getStringCommandParts(command);

        if (commandPartsValid(parts)){
            return Arrays.copyOfRange(parts, 1, parts.length);
        }
        return null;
    }

    protected boolean commandPartsValid(String[] parts){
        if (parts != null) {
            if (parts.length > 0) {
                return parts[0].equals(commandPrefix + commandName);
            }
        }
        return false;
    }

    public String getName() {
        return commandName;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }
}
