package game.contract;

import bot.menu.AbstractContractMenu;
import game.AnimeCardsGame;
import game.Player;
import org.hibernate.Session;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractContract implements ContractInterface {

    private boolean finished = false;

    protected String senderId;
    protected boolean senderConfirmed = false;
    protected String receiverId;
    protected boolean recipientConfirmed = false;

    private final Class<? extends AbstractContractMenu<? extends AbstractContract>> menuClass;
    protected AbstractContractMenu<? extends AbstractContract> menu;

    public AbstractContract(String senderId,
                            String receiverId,
                            Class<? extends AbstractContractMenu<? extends AbstractContract>> menuClass) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.menuClass = menuClass;
    }

    public AbstractContractMenu<? extends AbstractContract> buildMenu(AnimeCardsGame game){
        try {
            menu = menuClass.getConstructor(AnimeCardsGame.class, getClass()).newInstance(game, this);
            return menu;

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    protected Player getReceiver(AnimeCardsGame game) {
        return game.getPlayer(receiverId);
    }

    protected Player getSender(AnimeCardsGame game) {
        return game.getPlayer(senderId);
    }

    @Override
    public boolean isOwner(String userId) {
        return senderId.equals(userId) ||
                receiverId.equals(userId);
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
    public void confirm(AnimeCardsGame game, String userId) {
        setPlayerConfirmed(userId);
        if (!isFinished() && isConfirmed()){
            Session session = game.getDatabaseSession();
            session.beginTransaction();

            Player sender = getSender(game);
            Player receiver = getReceiver(game);

            finished = finish(session, sender, receiver);

            session.merge(sender);
            session.merge(receiver);

            session.getTransaction().commit();
        }
    }

    private void setPlayerConfirmed(String playerId) {
        if      (senderId.equals(playerId))     senderConfirmed = true;
        else if (receiverId.equals(playerId))  recipientConfirmed = true;
    }

    @Override
    public void discard() {
        finished = true;
    }

    @Override
    public String getMenuDescription(){
        return getMoreInfo();
    }

    protected void updateMenu() {

    }
}
