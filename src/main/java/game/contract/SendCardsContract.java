package game.contract;

import game.AnimeCardsGame;
import game.cards.CardPersonal;

import java.util.List;

public class SendCardsContract extends AbstractContract implements ContractInterface {

    private final List<CardPersonal> cards;

    public SendCardsContract(String senderId, String recipientId, List<CardPersonal> objSent) {
        super(senderId, recipientId);
        this.cards = objSent;
    }

    @Override
    public void complete(AnimeCardsGame game) {

    }

    @Override
    public void discard() {

    }

    @Override
    public String getMoreInfo() {
        return "more contract info";
    }

    public List<CardPersonal> getCards() {
        return cards;
    }
}
