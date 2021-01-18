package game.cards;


import game.MappedObjectManager;

import java.util.List;
import java.util.Timer;

public class CardDropManager extends MappedObjectManager<String, CardDropActivity> {

    int fightSeconds = 10;
    Timer fightTimer;

    public CardDropManager() {
        super(CardDropActivity.class);
        fightTimer = new Timer(true);
    }

    public void add(String messageId, CardDropActivity cardDropActivity) {
        addElement(messageId, cardDropActivity);

        fightTimer.scheduleAtFixedRate(
                new DropCardTimer(this, messageId, fightSeconds),
                0, 1000
        );
    }

    public List<CardGlobal> getCards(String messageId) {
        if (isElementExists(messageId)){
            return getElement(messageId).getCards();
        }
        return null;
    }
}
