package game.contract;

import game.AnimeCardsGame;

public interface ContractInterface {
    void confirm(AnimeCardsGame game, String playerId);
    boolean isConfirmed();

    boolean finish(AnimeCardsGame game);
    boolean isFinished();

    void discard();

    String getMoreInfo();
}
