package bot.menu;

import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.contract.CardForCardContract;

public class CardForCardContractMenu extends AbstractContractMenu<CardForCardContract> {

    public CardForCardContractMenu(AnimeCardsGame game, CardForCardContract contract) {
        super(game, CardForCardContract.class, contract);
    }

    @Override
    public void sendMenu(CommandEvent event) {
        String description = contract.getMoreInfo();
        EventHandlerButtonMenu menu = buildMenu(event, "Exchange cards", description);
        displayMenu(event.getChannel(), menu);
    }
}
