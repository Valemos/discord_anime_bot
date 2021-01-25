package game.contract;

import bot.menu.SendCardsContractMenu;
import game.Player;
import game.cards.CardPersonal;
import org.hibernate.Session;

import java.util.List;
import java.util.stream.Collectors;

public class SendCardsContract extends AbstractContract implements ContractInterface {

    private final List<CardPersonal> cards;

    public SendCardsContract(String senderId, String recipientId, List<CardPersonal> cards) {
        super(senderId, recipientId, SendCardsContractMenu.class);
        this.cards = cards;
    }

    @Override
    public boolean isConfirmed() {
        return senderConfirmed;
    }

    @Override
    public boolean finish(Session session, Player sender, Player receiver) {
        for (CardPersonal card : cards) {
            sender.getCards().remove(card);
            receiver.addCard(card);
        }
        return true;
    }

    @Override
    public String getMoreInfo() {
        return cards.stream()
                .map(CardPersonal::getFullDescription)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String getMenuDescription(){
        return getLimitedDescription(cards);
    }

    private String getLimitedDescription(List<CardPersonal> cards) {
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

    public List<CardPersonal> getCards() {
        return cards;
    }
}
