package bot.commands.user.squadron;

import bot.commands.AbstractCommand;
import bot.commands.arguments.MultipleIdentifiersArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardPersonal;
import game.player_objects.squadron.Squadron;

public class SquadronAddCommand extends AbstractCommand<MultipleIdentifiersArguments> {

    public SquadronAddCommand(AnimeCardsGame game) {
        super(game, MultipleIdentifiersArguments.class);
        name = "squadronadd";
        aliases = new String[]{"sqa"};
        guildOnly = false;
    }

    @Override
    public void handle(CommandEvent event) {
        Squadron squadron = game.getOrCreateSquadron(player);

        if(squadron.getPatrol().isStarted()){
            sendMessage(event, "stop patrol to edit squadron");
            return;
        }

        StringBuilder msgBuilder = new StringBuilder();
        for (String cardId : commandArgs.multipleIds){
            if (squadron.isFull()){
                msgBuilder.append("cannot add more cards to squadron");
                break;
            }

            CardPersonal card = game.getCardsPersonal().getById(cardId);

            if (card != null && card.getOwner().equals(player)){
                game.addSquadronMember(player, card);
                msgBuilder.append(card.getCharacterInfo().getFullName()).append(" added to squadron\n");
            }else{
                msgBuilder.append(cardId).append(" not found in player collection\n");
            }
        }

        sendMessage(event, msgBuilder.toString());
    }
}
