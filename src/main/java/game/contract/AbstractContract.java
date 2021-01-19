package game.contract;

import game.AnimeCardsGame;
import game.Player;

public abstract class AbstractContract implements ContractInterface {

    private boolean finished = false;

    protected String senderId;
    protected boolean senderConfirmed = false;
    protected String recipientId;
    protected boolean recipientConfirmed = false;

    public AbstractContract(String senderId, String recipientId) {
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public boolean isConfirmed() {
        return senderConfirmed && recipientConfirmed;
    }

    @Override
    public void confirm(AnimeCardsGame game, String playerId) {
        setPlayerConfirmed(playerId);
        if (!isFinished() && isConfirmed()){
            finished = finish(game);
        }
    }

    private void setPlayerConfirmed(String playerId) {
        if      (senderId.equals(playerId))     senderConfirmed = true;
        else if (recipientId.equals(playerId))  recipientConfirmed = true;
    }

    @Override
    public void discard() {
        finished = true;
    }

    protected Player getRecipient(AnimeCardsGame game) {
        return game.getPlayer(recipientId);
    }

    protected Player getSender(AnimeCardsGame game) {
        return game.getPlayer(senderId);
    }
}
