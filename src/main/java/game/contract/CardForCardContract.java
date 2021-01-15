package game.contract;

import game.AnimeCardsGame;
import game.cards.CardPersonal;

public class CardForCardContract extends AbstractContract {

    private final CardPersonal cardSending;
    private final CardPersonal cardReceiving;

    public CardForCardContract(String senderId, String recipientId, CardPersonal sending, CardPersonal receiving) {
        super(senderId, recipientId);
        cardSending = sending;
        cardReceiving = receiving;
    }

    @Override
    public void complete(AnimeCardsGame game) {

    }

    @Override
    public void discard() {

    }

    @Override
    public String getMoreInfo() {
        return null;
    }

    public CardPersonal getCardSending() {
        return cardSending;
    }

    public CardPersonal getCardReceiving() {
        return cardReceiving;
    }
}
