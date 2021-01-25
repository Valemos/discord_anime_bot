package game.contract;

import bot.menu.CardForCardContractMenu;
import game.Player;
import game.cards.CardPersonal;
import org.hibernate.Session;

public class CardForCardContract extends AbstractContract {

    private final CardPersonal cardSending;
    private final CardPersonal cardReceiving;

    public CardForCardContract(String senderId, String recipientId, CardPersonal sending, CardPersonal receiving) {
        super(senderId, recipientId, CardForCardContractMenu.class);
        cardSending = sending;
        cardReceiving = receiving;
    }

    @Override
    public boolean finish(Session session, Player sender, Player receiver) {
        sender.addCard(cardReceiving);
        receiver.getCards().remove(cardReceiving);

        receiver.addCard(cardSending);
        sender.getCards().remove(cardSending);
        return true;
    }

    @Override
    public String getMoreInfo() {
        return "Card of player " + senderId + '\n' +
                cardSending.getIdNameStats() + '\n' +
                "Card of player " + receiverId + '\n' +
                cardReceiving.getIdNameStats();
    }
}
