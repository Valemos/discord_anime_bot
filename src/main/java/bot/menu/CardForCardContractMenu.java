package bot.menu;

import game.AnimeCardsGame;
import game.contract.CardForCardContract;

public class CardForCardContractMenu extends AbstractContractMenu<CardForCardContract> {
    public CardForCardContractMenu(AnimeCardsGame game, CardForCardContract contract) {
        super(game, contract, "Exchange cards");
    }
}
