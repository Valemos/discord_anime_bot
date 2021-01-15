package game.contract;

import game.AnimeCardsGame;
import game.cards.CardPersonal;

import java.util.LinkedList;
import java.util.List;

public class MultiTradeContract extends AbstractContract {

    List<CardPersonal> cards;

    public MultiTradeContract(String senderId, String recipientId) {
        super(senderId, recipientId);

        cards = new LinkedList<>();
    }

    public String getDescription() {
        return null;
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
}
