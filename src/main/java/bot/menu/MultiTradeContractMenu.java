package bot.menu;

import game.AnimeCardsGame;
import game.contract.MultiTradeContract;

public class MultiTradeContractMenu extends AbstractContractMenu<MultiTradeContract> {
    public MultiTradeContractMenu(AnimeCardsGame game, MultiTradeContract contract) {
        super(game, contract, "Trading");
    }
}
