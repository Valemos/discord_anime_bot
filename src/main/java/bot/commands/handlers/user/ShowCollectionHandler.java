package bot.commands.handlers.user;

import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.handlers.BotCommandHandler;
import bot.commands.handlers.DefaultMessageArguments;
import bot.commands.handlers.EmptyMessageArguments;

public class ShowCollectionHandler extends BotCommandHandler {

    public ShowCollectionHandler() {
        super(new CommandInfo("collection", "c"));
    }

    @Override
    public void handleCommand(CommandParameters parameters) {

    }
}
