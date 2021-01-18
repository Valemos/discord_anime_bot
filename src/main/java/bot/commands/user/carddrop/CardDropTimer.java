package bot.commands.user.carddrop;

import game.cards.CardDropManager;

import java.util.TimerTask;

public class CardDropTimer extends TimerTask {
    private final String messageId;
    private final CardDropManager cardDropManager;

    public CardDropTimer(CardDropManager cardDropManager, String messageId, long fightSeconds) {
        this.cardDropManager = cardDropManager;
        this.messageId = messageId;
    }

    @Override
    public void run() {
        if (cardDropManager.isElementExists(messageId)){
            cardDropManager.getElement(messageId).finishFight();
            cardDropManager.removeElement(messageId);
            cancel(); // finish timer execution
        }
    }
}
