package bot.commands.creator;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;

public class JoinAsTesterCommand extends AbstractCommand<AbstractCommand.NoArgumentsConfig> {

    public JoinAsTesterCommand(AnimeCardsGame game) {
        super(game, NoArgumentsConfig.class);
        name = "test";
    }

    @Override
    protected void handle(CommandEvent event) {
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
