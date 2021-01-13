package bot.commands.user;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardPersonal;
import game.squadron.Squadron;

public class SquadronAddCommand extends AbstractCommand<MultipleCardsArguments> {

    public SquadronAddCommand(AnimeCardsGame game) {
        super(game, MultipleCardsArguments.class);
        name = "squadronadd";
        aliases = new String[]{"sqa"};
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        Squadron squadron = game.getSquadron(player);

        StringBuilder msgBuilder = new StringBuilder();
        for (String cardId : commandArgs.cardIds){
            CardPersonal card = game.getPersonalCard(player, cardId);

            if (card != null){
                if (squadron.addCard(card)){
                    msgBuilder.append(card.getCharacterInfo().getFullName()).append(" added to squadron\n");
                }else{
                    msgBuilder.append("cannot add more cards to squadron");
                    break;
                }
            }else{
                msgBuilder.append(cardId).append(" not found in player collection");
            }
        }

        sendMessage(event, msgBuilder.toString());
    }
}
