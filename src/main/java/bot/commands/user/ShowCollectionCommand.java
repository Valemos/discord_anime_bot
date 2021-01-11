package bot.commands.user;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CharacterCardPersonal;
import game.cards.PersonalCollection;

public class ShowCollectionCommand extends AbstractCommand<ShowCollectionCommand.Arguments> {

    public static class Arguments {
    }

    public ShowCollectionCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "collection";
        aliases = new String[]{"c"};
    }

    @Override
    protected void handle(CommandEvent event) {
        PersonalCollection collection = game.getPlayerCollection(game.createNewPlayer("10"));

        StringBuilder b = new StringBuilder();
        b.append("User collection");
        int counter = 1;
        for (CharacterCardPersonal card : collection.getCards()){
            b.append('\n').append(counter++).append('.');
            b.append(card.getOneLineRepresentationString());
        }

        event.getChannel().sendMessage(b.toString()).queue();
    }
}
