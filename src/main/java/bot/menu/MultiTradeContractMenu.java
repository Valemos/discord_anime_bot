package bot.menu;

import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.contract.MultiTradeContract;

public class MultiTradeContractMenu extends AbstractContractMenu<MultiTradeContract> {

    public MultiTradeContractMenu(AnimeCardsGame game, MultiTradeContract contract) {
        super(game, MultiTradeContract.class, contract);
    }

    @Override
    public void sendMenu(CommandEvent event) {
        EventHandlerButtonMenu menu = buildMenu(event, "Trading", contract.getDescription());
        displayMenu(event.getChannel(), menu);
    }
}
