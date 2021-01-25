package game.contract;

import game.AnimeCardsGame;
import game.Player;
import org.hibernate.Session;

public interface ContractInterface {
    boolean isOwner(String userId);
    void confirm(AnimeCardsGame game, String userId);
    boolean isConfirmed();
    boolean finish(Session session, Player sender, Player receiver);
    boolean isFinished();
    void discard();
    String getMoreInfo();
    String getMenuDescription();
}
