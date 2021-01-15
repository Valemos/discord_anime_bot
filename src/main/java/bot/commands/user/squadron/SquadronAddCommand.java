package bot.commands.user.squadron;

import bot.commands.AbstractCommand;
import bot.commands.arguments.MultipleIdentifiersArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardPersonal;
import game.squadron.Squadron;

public class SquadronAddCommand extends AbstractCommand<MultipleIdentifiersArguments> {

    public SquadronAddCommand(AnimeCardsGame game) {
        super(game, MultipleIdentifiersArguments.class);
        name = "squadronadd";
        aliases = new String[]{"sqa"};
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        Squadron squadron = game.getSquadron(player);

        StringBuilder msgBuilder = new StringBuilder();
        for (String cardId : commandArgs.multipleIds){
            if (squadron.isFull()){
                msgBuilder.append("cannot add more cards to squadron");
                break;
            }

            CardPersonal card = game.getCardPersonal(player.getId(), cardId);

            if (card != null){
                squadron.addCard(card);
                msgBuilder.append(card.getCharacterInfo().getFullName()).append(" added to squadron\n");
            }else{
                msgBuilder.append(cardId).append(" not found in player collection");
            }
        }

        sendMessage(event, msgBuilder.toString());
    }
}
