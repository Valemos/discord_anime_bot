package game.cards;


import bot.commands.user.carddrop.CardDropTimer;
import game.AnimeCardsGame;
import org.hibernate.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class CardDropManager {

    long fightSeconds = 10;
    private Timer fightTimer;
    private final Session dbSession;
    private final AnimeCardsGame game;
    private final Map<String, CardDropActivity> cardDropActivities = new HashMap<>();

    public CardDropManager(AnimeCardsGame game, Session dbSession) {
        this.game = game;
        this.dbSession = dbSession;
        fightTimer = new Timer(true);
    }

    public void startActivity(String messageId, CardDropActivity cardDropActivity) {
        addElement(messageId, cardDropActivity);
        fightTimer.schedule(new CardDropTimer(game, this, messageId),fightSeconds * 1000);
    }

    public List<CardGlobal> getCards(String messageId) {
        if (isExists(messageId)){
            return getActivity(messageId).getCards();
        }
        return null;
    }

    public long getFightSeconds() {
        return fightSeconds;
    }

    public void setFightSeconds(long fightSeconds) {
        this.fightSeconds = fightSeconds;
    }

    public Session getDatabaseSession() {
        return dbSession;
    }

    public void setFightTimer(Timer fightTimer) {
        this.fightTimer = fightTimer;
    }


    protected void addElement(String key, CardDropActivity element) {
        cardDropActivities.put(key, element);
    }

    public synchronized void remove(String key) {
        cardDropActivities.remove(key);
    }

    public synchronized boolean isExists(String key) {
        return cardDropActivities.containsKey(key);
    }

    public synchronized CardDropActivity getActivity(String key) {
        return cardDropActivities.getOrDefault(key, null);
    }
}
