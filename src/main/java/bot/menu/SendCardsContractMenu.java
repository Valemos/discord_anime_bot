package bot.menu;

import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardPersonal;
import game.contract.SendCardsContract;

import java.util.List;

public class SendCardsContractMenu extends AbstractContractMenu<SendCardsContract> {

    public SendCardsContractMenu(AnimeCardsGame game, SendCardsContract contract) {
        super(game, SendCardsContract.class, contract);
    }

    @Override
    public void sendMenu(CommandEvent event) {
        String cardsDescription = getCardsDescription(contract.getCards());
        EventHandlerButtonMenu menu = buildMenu(event, "Send cards", cardsDescription);
        displayMenu(event.getChannel(), menu);
    }

    private String getCardsDescription(List<CardPersonal> cards) {
        final int maxCardLines = 10;
        int currentLine = 0;
        boolean hasMoreCards = false;

        StringBuilder builder = new StringBuilder();
        for (CardPersonal card : cards) {
            if (currentLine == maxCardLines){
                hasMoreCards = true;
                break;
            }

            builder.append(card.getIdName());

            ++currentLine;
        }

        if (hasMoreCards){
            builder.append("and ").append(cards.size() - 10).append(" more");
        }
        return builder.toString();
    }
}
