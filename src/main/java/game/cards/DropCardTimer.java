package game.cards;

import java.util.TimerTask;

public class DropCardTimer extends TimerTask {
    private final String messageId;
    private final CardDropManager cardDropManager;
    private int secondsLeft;

    public DropCardTimer(CardDropManager cardDropManager, String messageId, int fightSeconds) {
        this.cardDropManager = cardDropManager;
        this.messageId = messageId;
        secondsLeft = fightSeconds;
    }

    @Override
    public void run() {
        if (secondsLeft > 0){
            cardDropManager.getElement(messageId).setSecondsLeft(secondsLeft);
            --secondsLeft;

        } else if (cardDropManager.isElementExists(messageId)){
            cardDropManager.getElement(messageId).finishFight();
            cardDropManager.removeElement(messageId);
            cancel(); // finish timer execution
        }
    }
}
