package bot.commands;


import game.CharacterCard;
import game.GlobalCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DropHandler extends BotCommandHandler {

    public DropHandler() {
        commandName = "drop";
    }

    @Override
    public boolean isArgumentsValid(String[] arguments) {
        return arguments.length == 0;
    }

    @Override
    public void handleCommand(CommandArguments args) {

        GlobalCollection collection = args.game.getGlobalCollection();
        List<CharacterCard> cards = collection.getRandomCards(3);

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Dropped new 3 cards:");

        for (CharacterCard card : cards){
            messageBuilder.append('\n');
            messageBuilder.append(card.getOneLineString());
        }

        args.channel.sendMessage(messageBuilder.toString()).queue();
    }
}
