package bot.commands.user.trading;

import game.AnimeCardsGame;
import game.Player;
import game.contract.AbstractContract;
import game.contract.MultiTradeContract;

public interface InterfaceMultiTradeTest {
    Player tester();
    AnimeCardsGame game();

    default MultiTradeContract getMultiTrade() {
        return getContract(tester(), MultiTradeContract.class);
    }

    default <T extends AbstractContract> T getContract(Player tester, Class<T> contractClass) {
        return game().getContractsManager().getForUser(contractClass, tester.getId());
    }
}
