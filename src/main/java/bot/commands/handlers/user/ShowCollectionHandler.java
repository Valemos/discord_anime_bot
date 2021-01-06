package bot.commands.handlers.user;

import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.handlers.BotCommandHandler;

public class ShowCollectionHandler extends BotCommandHandler {

    public ShowCollectionHandler() {
        commandInfo = new CommandInfo("collection", "c");
    }

    @Override
    public void handleCommand(CommandParameters parameters) {

    }
}
