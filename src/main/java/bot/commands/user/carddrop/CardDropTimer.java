package bot.commands.user.carddrop;

import game.AnimeCardsGame;
import game.cards.CardDropManager;

import java.util.TimerTask;

public class CardDropTimer extends TimerTask {
    private final String messageId;
    private final CardDropManager cardDropManager;
    private AnimeCardsGame game;

    public CardDropTimer(AnimeCardsGame game, CardDropManager cardDropManager, String messageId) {
        this.game = game;
        this.cardDropManager = cardDropManager;
        this.messageId = messageId;
    }

    @Override
    public void run() {
        if (cardDropManager.isElementExists(messageId)){
            cardDropManager.getElement(messageId).finishFight(game);
            cardDropManager.removeElement(messageId);
            cancel(); // finish timer execution
        }
    }
}
