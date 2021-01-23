package bot.commands.user.carddrop;

import game.AnimeCardsGame;
import game.cards.CardDropManager;

import java.util.TimerTask;

public class CardDropTimer extends TimerTask {
    private final String messageId;
    private final CardDropManager cardDropManager;
    private final AnimeCardsGame game;

    public CardDropTimer(AnimeCardsGame game, CardDropManager cardDropManager, String messageId) {
        this.game = game;
        this.cardDropManager = cardDropManager;
        this.messageId = messageId;
    }

    @Override
    public void run() {
        if (cardDropManager.isExists(messageId)){
            cardDropManager.getActivity(messageId).finishFights(game);
            cardDropManager.remove(messageId);
            cancel(); // finish timer execution
        }
    }
}
