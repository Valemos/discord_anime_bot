package bot.commands;

import bot.AccessLevel;

public class CreateCardHandler extends BotCommandHandler{

    public CreateCardHandler() {
        commandName = "createcard";
        accessLevel = AccessLevel.CREATOR;
    }

    @Override
    public void handleCommand(CommandArguments arguments) {

    }
}
