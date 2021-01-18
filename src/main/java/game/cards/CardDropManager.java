package game.cards;


import bot.commands.user.carddrop.CardDropTimer;
import game.MappedObjectManager;

import java.util.List;
import java.util.Timer;

public class CardDropManager extends MappedObjectManager<String, CardDropActivity> {

    long fightSeconds = 10;
    private final Timer fightTimer;


    public CardDropManager() {
        super(CardDropActivity.class);
        fightTimer = new Timer(true);
    }

    public void add(String messageId, CardDropActivity cardDropActivity) {
        addElement(messageId, cardDropActivity);
        fightTimer.schedule(new CardDropTimer(this, messageId, fightSeconds),fightSeconds * 1000);
    }

    public List<CardGlobal> getCards(String messageId) {
        if (isElementExists(messageId)){
            return getElement(messageId).getCards();
        }
        return null;
    }

    public long getFightSeconds() {
        return fightSeconds;
    }
}
