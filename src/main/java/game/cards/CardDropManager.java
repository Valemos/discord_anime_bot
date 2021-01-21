package game.cards;


import bot.commands.user.carddrop.CardDropTimer;
import game.AnimeCardsGame;
import game.MappedObjectManager;
import org.hibernate.Session;

import java.util.List;
import java.util.Timer;

public class CardDropManager extends MappedObjectManager<String, CardDropActivity> {

    long fightSeconds = 10;
    private Timer fightTimer;
    private Session dbSession;
    private AnimeCardsGame game;

    public CardDropManager(AnimeCardsGame game, Session dbSession) {
        super(CardDropActivity.class);
        this.game = game;
        this.dbSession = dbSession;
        fightTimer = new Timer(true);
    }

    public void add(String messageId, CardDropActivity cardDropActivity) {
        addElement(messageId, cardDropActivity);
        fightTimer.schedule(new CardDropTimer(game, this, messageId),fightSeconds * 1000);
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

    public Session getDatabaseSession() {
        return dbSession;
    }

    public void setFightTimer(Timer fightTimer) {
        this.fightTimer = fightTimer;
    }
}
