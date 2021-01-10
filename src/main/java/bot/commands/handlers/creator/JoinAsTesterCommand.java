package bot.commands.handlers.creator;

import bot.PlayerAccessLevel;
import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.handlers.AbstractBotCommand;
import bot.commands.handlers.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CharacterCardGlobal;
import game.cards.PersonalCollection;

import java.util.List;

public class JoinAsTesterCommand extends AbstractCommand {

    public JoinAsTesterCommand(AnimeCardsGame game) {
        super(game);
        name = "test";
    }

    @Override
    protected void execute(CommandEvent event) {
//        parameters.player.setAccessLevel(PlayerAccessLevel.CREATOR);
//
//        PersonalCollection collection = parameters.game.getPlayerCollection(parameters.player);
//
//        List<CharacterCardGlobal> allCards = parameters.game.getGlobalCollection().getRandomCards(5);
//
//        float counter = 0;
//        for (CharacterCardGlobal card : allCards){
//            collection.addCard(card.getPersonalCardForPickDelay(parameters.player.getId(), counter++));
//        }
    }
}
