package bot.commands;

import bot.AccessLevel;
import game.CardStats;
import game.CharacterCard;

public class CreateCardHandler extends BotCommandHandler{

    public CreateCardHandler() {
        commandName = "createcard";
        accessLevel = AccessLevel.CREATOR;
    }

    @Override
    public boolean isArgumentsValid(String[] arguments) {
        return arguments.length == 3;
    }

    @Override
    public void handleCommand(CommandArguments args) {
        CharacterCard new_card = new CharacterCard(
                args.commandArgs[0],
                args.commandArgs[1],
                args.commandArgs[2],
                new CardStats());

        args.game.addCard(new_card);
    }
}
