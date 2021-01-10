package bot.commands.handlers.user;

import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.handlers.AbstractCommand;
import bot.commands.parsing.*;
import bot.commands.handlers.AbstractBotCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.Player;
import game.cards.CharacterCardPersonal;
import game.cards.PersonalCollection;

public class ShowCollectionCommand extends AbstractCommand {

    public ShowCollectionCommand(AnimeCardsGame game) {
        super(game);
        name = "collection";
        aliases = new String[]{"c"};
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO get player by user id

        PersonalCollection collection = game.getPlayerCollection(player);

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
