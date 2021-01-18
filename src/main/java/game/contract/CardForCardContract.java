package game.contract;

import game.AnimeCardsGame;
import game.Player;
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
    public boolean finish(AnimeCardsGame game) {
        Player sender = getSender(game);
        Player recipient = getRecipient(game);

        sender.addCard(cardReceiving);
        recipient.addCard(cardSending);

        return true;
    }

    @Override
    public String getMoreInfo() {
        return "Card of player " + senderId + '\n' +
                cardSending.getIdNameStats() + '\n' +
                "Card of player " + recipientId + '\n' +
                cardReceiving.getIdNameStats();
    }
}
