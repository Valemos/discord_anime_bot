package bot.commands;


import game.CharacterCard;
import game.GlobalCollection;

import java.util.List;

public class DropHandler extends BotCommandHandler {

    public DropHandler() {
        commandInfo = new CommandInfo("drop");
    }

    @Override
    public void handleCommand(CommandParameters parameters) {

        GlobalCollection collection = parameters.game.getGlobalCollection();
        List<CharacterCard> cards = collection.getRandomCards(3);

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Dropped new 3 cards:");

        for (CharacterCard card : cards){
            messageBuilder.append('\n');
            messageBuilder.append(card.getOneLineString());
        }

        parameters.channel.sendMessage(messageBuilder.toString()).queue();
    }
}
