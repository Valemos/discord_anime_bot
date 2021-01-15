package game.contract;

import game.AnimeCardsGame;

public interface ContractInterface {
    void complete(AnimeCardsGame game);
    void discard();
    String getMoreInfo();
}
