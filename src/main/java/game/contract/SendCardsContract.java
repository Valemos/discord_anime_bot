package game.contract;

import game.AnimeCardsGame;
import game.Player;
import game.cards.CardPersonal;

import java.util.List;
import java.util.stream.Collectors;

public class SendCardsContract extends AbstractContract implements ContractInterface {

    private final List<CardPersonal> cards;

    public SendCardsContract(String senderId, String recipientId, List<CardPersonal> objSent) {
        super(senderId, recipientId);
        this.cards = objSent;
    }

    @Override
    public boolean isConfirmed() {
        return senderConfirmed;
    }

    @Override
    public boolean finish(AnimeCardsGame game) {
        Player recipient = getRecipient(game);

        for (CardPersonal card : cards) {
            recipient.addCard(card);
        }

        return true;
    }

    @Override
    public String getMoreInfo() {
        return cards.stream()
                .map(CardPersonal::getFullDescription)
                .collect(Collectors.joining("\n"));
    }

    public List<CardPersonal> getCards() {
        return cards;
    }
}
