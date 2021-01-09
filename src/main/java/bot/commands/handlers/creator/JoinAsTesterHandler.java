package bot.commands.handlers.creator;

import bot.AccessLevel;
import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.handlers.BotCommandHandler;
import game.cards.CharacterCardGlobal;
import game.cards.PersonalCollection;

import java.util.List;

public class JoinAsTesterHandler extends BotCommandHandler {

    public JoinAsTesterHandler() {
        super(new CommandInfo("test"));
    }

    @Override
    public void handle(CommandParameters parameters) {
        parameters.player.setAccessLevel(AccessLevel.CREATOR);

        PersonalCollection coll = parameters.game.getPlayerCollection(parameters.player);

        List<CharacterCardGlobal> allCards = parameters.game.getGlobalCollection().getRandomCards(5);

        float counter = 0;
        for (CharacterCardGlobal card : allCards){
            coll.addCard(card.getPersonalCardForPickDelay(parameters.player.getId(), counter++));
        }
    }
}
